package com.agrifederation.repository;

import com.agrifederation.config.DatabaseConfig;
import com.agrifederation.dto.ActivityMemberAttendanceDTO;
import com.agrifederation.dto.ActivityRequest;
import com.agrifederation.dto.MemberDescriptionDTO;
import com.agrifederation.entity.ActivityMemberAttendance;
import com.agrifederation.entity.CollectivityActivity;
import com.agrifederation.entity.MonthlyRecurrenceRule;
import com.agrifederation.enums.*;
import lombok.Data;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Repository
@Data
public class CollectivityActivityRepository {
    private final DatabaseConfig databaseConfig;

    public List<CollectivityActivity> createActivities(String id, List<ActivityRequest> activitiesGivenList) {
        String activityQuery = """
                INSERT INTO collectivity_activity (id, label, activity_type,
                executive_date, id_collectivity, id_monthly_recurrence) VALUES(?, ?, ?::activity_type, ?, ?, ?)
                RETURNING id AS id_activity;
                """;

        String activityOccupationQuery = """
                INSERT INTO activity_occupation (id_activity, occupation) VALUES(?, ?)
                """;

        String monthly_recurrence = """
                INSERT INTO monthly_recurrence_rule(id, week_ordinal, day_of_week)
                VALUES(?, ?, ?::days);
                """;
        List<CollectivityActivity> addedActivityList = new ArrayList<>();
        try (Connection connection = databaseConfig.getConnection()) {
            connection.setAutoCommit(false);
            try (PreparedStatement activityStmt = connection.prepareStatement(activityQuery);
                PreparedStatement activityOccupationStmt = connection.prepareStatement(activityOccupationQuery);
                PreparedStatement recurrenceStmt = connection.prepareStatement(monthly_recurrence);) {
                for(ActivityRequest activity : activitiesGivenList) {
                    activityStmt.setString(1, UUID.randomUUID().toString());
                    activityStmt.setString(2, activity.getLabel());
                    activityStmt.setString(3, activity.getActivityType().name());
                    LocalDate executiveDate = activity.getExecutiveDate();
                    activityStmt.setDate(4, executiveDate == null ? null : Date.valueOf(executiveDate));
                    activityStmt.setString(5, id);

                    String recurrenceId = null;

                    if(activity.getMonthlyRecurrenceRule() != null) {
                        recurrenceId = UUID.randomUUID().toString();
                        recurrenceStmt.setString(1, recurrenceId);
                        recurrenceStmt.setInt(2, activity.getMonthlyRecurrenceRule().getWeekOrdinal());
                        recurrenceStmt.setString(3, activity.getMonthlyRecurrenceRule().getDayOfWeek().name());
                        recurrenceStmt.executeUpdate();
                    }

                    activityStmt.setString(6, recurrenceId);
                    ResultSet resultSet = activityStmt.executeQuery();
                    while (resultSet.next()) {
                        CollectivityActivity collectivityActivity = new CollectivityActivity();
                        for(ActivityRequest activityRequest : activitiesGivenList) {
                            collectivityActivity.setId(resultSet.getString("id_activity"));
                            collectivityActivity.setLabel(activityRequest.getLabel());
                            collectivityActivity.setActivityType(activityRequest.getActivityType() == null ? null : activityRequest.getActivityType());
                            collectivityActivity.setExecutiveDate(activityRequest.getExecutiveDate());
                            collectivityActivity.setIdCollectivity(activityRequest.getIdCollectivity());
                            List<Occupation> occupationList = new ArrayList<>();
                            String returnedId = resultSet.getString("id_activity");
                            if(activity.getMemberOccupationConcerned() != null && !activity.getMemberOccupationConcerned().isEmpty()) {
                                for(Occupation occupation : activity.getMemberOccupationConcerned()) {
                                    activityOccupationStmt.setString(1, returnedId);
                                    activityOccupationStmt.setString(2, occupation.name());
                                    activityOccupationStmt.addBatch();
                                    occupationList.add(occupation);
                                }
                            }
                            collectivityActivity.setMemberOccupationConcerned(occupationList);
                            MonthlyRecurrenceRule monthlyRecurrenceRule = new MonthlyRecurrenceRule();
                            monthlyRecurrenceRule.setWeekOrdinal(activity.getMonthlyRecurrenceRule().getWeekOrdinal() == null ? null : activity.getMonthlyRecurrenceRule().getWeekOrdinal());
                            monthlyRecurrenceRule.setDayOfWeek(activity.getMonthlyRecurrenceRule().getDayOfWeek());
                            collectivityActivity.setMonthlyRecurrenceRule(monthlyRecurrenceRule);
                            addedActivityList.add(collectivityActivity);
                        }
                    }
                }
                activityOccupationStmt.executeBatch();
                connection.commit();
            } catch (SQLException e) {
                connection.rollback();
                throw  new RuntimeException(e);
            }
            return addedActivityList;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<CollectivityActivity> getActivitiesCollectivityById(String collectivityId) {
        List<CollectivityActivity> activities = new ArrayList<>();

        String query = """
                SELECT ca.id,
                    ca.label,
                    ca.activity_type,
                    ca.executive_date,
                    ca.id_collectivity,
                    ca.id_monthly_recurrence,
                    mrr.week_ordinal,
                    mrr.day_of_week
                FROM collectivity_activity ca
                LEFT JOIN monthly_recurrence_rule mrr ON ca.id_monthly_recurrence = mrr.id
                WHERE ca.id_collectivity = ?
                ORDER BY ca.executive_date DESC
                """;
        try(Connection connection = databaseConfig.getConnection();
        PreparedStatement statement = connection.prepareStatement(query);) {
            statement.setString(1, collectivityId);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                CollectivityActivity activity = new CollectivityActivity();
                activity.setId(resultSet.getString("id"));
                activity.setLabel(resultSet.getString("label"));
                activity.setActivityType(Type.valueOf(resultSet.getString("activity_type")));
                Date executiveDate = resultSet.getDate("executive_date");
                if(executiveDate != null) {
                    activity.setExecutiveDate(executiveDate.toLocalDate());
                }

                activity.setIdCollectivity(resultSet.getString("id_collectivity"));
                activity.setIdMonthlyRecurrence(resultSet.getString("id_monthly_recurrence"));

                if(resultSet.getString("id_monthly_recurrence") != null) {
                    MonthlyRecurrenceRule rule = new MonthlyRecurrenceRule();
                    rule.setWeekOrdinal(resultSet.getInt("week_ordinal"));
                    rule.setDayOfWeek(DaysEnum.valueOf(resultSet.getString("day_of_week")));
                    activity.setMonthlyRecurrenceRule(rule);
                }
                activities.add(activity);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }


        return activities;
    }

    public List<ActivityMemberAttendanceDTO> getAttendanceWithMemberDetails(String activityId) {
        List<ActivityMemberAttendanceDTO> attendanceList = new ArrayList<>();

        String query = """
                SELECT aa.id_member, aa.status,
                       m.first_name, m.last_name, m.email, m.member_occupation
                FROM activity_attendance aa
                JOIN member m ON m.id = aa.id_member
                WHERE id_activity = ?
                """;

        try(Connection connection = databaseConfig.getConnection();
        PreparedStatement statement = connection.prepareStatement(query);) {
            statement.setString(1, activityId);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                ActivityMemberAttendanceDTO dto = new ActivityMemberAttendanceDTO();
                dto.setId(resultSet.getString("id_member"));

                MemberDescriptionDTO descriptionDTO = new MemberDescriptionDTO();
                descriptionDTO.setId(resultSet.getString("id_member"));
                descriptionDTO.setFirstName(resultSet.getString("first_name"));
                descriptionDTO.setLastName(resultSet.getString("last_name"));
                descriptionDTO.setEmail(resultSet.getString("email"));
                descriptionDTO.setOccupation(Occupation.valueOf(resultSet.getString("occupation")));
                dto.setMemberDescription(descriptionDTO);

                dto.setAttendanceStatus(AttendanceStatus.valueOf(resultSet.getString("status")));

                attendanceList.add(dto);

            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return attendanceList;
    }



}
