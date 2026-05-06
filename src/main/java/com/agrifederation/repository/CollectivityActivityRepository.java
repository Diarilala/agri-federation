package com.agrifederation.repository;

import com.agrifederation.config.DatabaseConfig;
import com.agrifederation.entity.CollectivityActivity;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Repository
@Data
@AllArgsConstructor
public class CollectivityActivityRepository {
    private DatabaseConfig databaseConfig;

    public List<CollectivityActivity> createActivities(String collectivityId, List<CollectivityActivity> activities) {
        List<CollectivityActivity> createdActivities = new ArrayList<>();

        String query = """
                    INSERT INTO collectivity_activity (id, collectivity_id, label, activity_type, member_occupation_concerned, week_ordinal, day_of_week, executive_date, created_at)
                    VALUES (?, ?, ?, ?, ?::activity_type, ?, ?::day_of_week, ?, ?)
                """;

        try(Connection con = databaseConfig.getConnection();
            PreparedStatement stmt = con.prepareStatement(query);){

            for (CollectivityActivity collectivityActivity : activities) {
                String activityId = UUID.randomUUID().toString();
                collectivityActivity.setId(activityId);

                stmt.setString(1, activityId);
                stmt.setString(2, collectivityId);
                stmt.setString(3, collectivityActivity.getLabel());
                stmt.setString(4, collectivityActivity.getActivityType().toString());
                stmt.setString(5, collectivityActivity.getMemberOccupationConcerned().toString());

                if (collectivityActivity.getWeekOrdinal() != null) {
                    stmt.setInt(6, collectivityActivity.getWeekOrdinal());
                } else  {
                    stmt.setNull(6, Types.INTEGER);
                }

                if (collectivityActivity.getDayOfWeek() != null) {
                    stmt.setInt(7, collectivityActivity.getDayOfWeek().getValue());
                } else   {
                    stmt.setNull(7, Types.INTEGER);
                }
                if (collectivityActivity.getExecutiveDate() != null) {
                    stmt.setDate(8, Date.valueOf(collectivityActivity.getExecutiveDate()));
                } else {
                    stmt.setNull(8, Types.DATE);
                }

                stmt.setTimestamp(9, Timestamp.valueOf(LocalDateTime.now()));
                stmt.executeUpdate();
                createdActivities.add(collectivityActivity);
            }


        } catch (SQLException e ) {
            throw new RuntimeException(e);
        }
        return createdActivities;
    }


}
