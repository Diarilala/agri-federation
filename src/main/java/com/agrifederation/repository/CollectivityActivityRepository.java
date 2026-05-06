package com.agrifederation.repository;

import com.agrifederation.config.DatabaseConfig;
import com.agrifederation.entity.CollectivityActivity;
import com.agrifederation.entity.MonthlyRecurrenceRule;
import com.agrifederation.enums.Occupation;
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
                RETURNING id AS id_activity;
                """;

        String activityOccupationQuery = """
                INSERT INTO activity_occupation (id_activity, occupation) VALUES(?, ?)
                """;

        String monthly_recurrence = """
                INSERT INTO monthly_recurrence_rule(id, week_ordinal, day_of_week)
                VALUES(?, ?, ?::days)
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
                        addedActivityList.add(collectivityActivity);
                        String returnedId = resultSet.getString("id_activity");
                        if(activity.getMemberOccupationConcerned() != null && activity.getMemberOccupationConcerned().isEmpty()) {
                            for(Occupation occupation : activity.getMemberOccupationConcerned()) {
                                activityOccupationStmt.setString(1, returnedId);
                                activityOccupationStmt.setString(2, occupation.name());
                                activityOccupationStmt.addBatch();
                            }
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
}
