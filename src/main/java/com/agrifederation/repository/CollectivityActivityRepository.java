package com.agrifederation.repository;

import com.agrifederation.config.DatabaseConfig;
import com.agrifederation.entity.CollectivityActivity;
import com.agrifederation.entity.MonthlyRecurrenceRule;
import com.agrifederation.enums.DayOfWeek;
import com.agrifederation.enums.DaysEnum;
import com.agrifederation.enums.Occupation;
import com.agrifederation.enums.Type;
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

    public List<CollectivityActivity> createActivities(String id, List<CollectivityActivity> activitiesGivenList) {
        String activityQuery = """
                INSERT INTO collectivity_activity (id, label, activity_type,
                executive_date, id_collectivity, id_monthly_recurrence) VALUES(?, ?, ?::activity_type, ?, ?, ?)
                RETURNING id AS id_activity, label,  activity_type, executive_date, id_collectivity;
                """;

        String activityOccupationQuery = """
                INSERT INTO activity_occupation (id_activity, occupation) VALUES(?, ?)
                RETURNING occupation;
                """;

        String monthly_recurrence = """
                INSERT INTO monthly_recurrence_rule(id, week_ordinal, day_of_week)
                VALUES(?, ?, ?::days)
                RETURNING week_ordinal, day_of_week;
                """;
        List<CollectivityActivity> addedActivityList = new ArrayList<>();
        try (Connection connection = databaseConfig.getConnection()) {
            connection.setAutoCommit(false);
            try (PreparedStatement activityStmt = connection.prepareStatement(activityQuery);
                PreparedStatement activityOccupationStmt = connection.prepareStatement(activityOccupationQuery);
                PreparedStatement recurrenceStmt = connection.prepareStatement(monthly_recurrence);) {
                for(CollectivityActivity activity : activitiesGivenList) {
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
                        collectivityActivity.setId(resultSet.getString("id"));
                        collectivityActivity.setLabel(resultSet.getString("label"));
                        String activityType = resultSet.getString("activity_type");
                        collectivityActivity.setActivityType(activityType == null ? null : Type.valueOf(activityType));
                        collectivityActivity.setExecutiveDate(LocalDate.parse("executive_date"));
                        collectivityActivity.setIdCollectivity("id_collectivity");
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
                        monthlyRecurrenceRule.setWeekOrdinal(resultSet.getInt("week_ordinal"));
                        String dayOfWeek = resultSet.getString("day_of_week");
                        monthlyRecurrenceRule.setDayOfWeek(dayOfWeek == null ? null : DaysEnum.valueOf(dayOfWeek));
                        collectivityActivity.setMonthlyRecurrenceRule(monthlyRecurrenceRule);
                        addedActivityList.add(collectivityActivity);
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

}
