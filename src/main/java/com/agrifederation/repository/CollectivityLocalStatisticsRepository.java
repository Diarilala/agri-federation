package com.agrifederation.repository;

import com.agrifederation.config.DatabaseConfig;
import com.agrifederation.entity.CollectivityLocalStatistics;
import com.agrifederation.enums.Occupation;
import lombok.Data;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Repository
@Data
public class CollectivityLocalStatisticsRepository {
    private final DatabaseConfig databaseConfig;

    public List<CollectivityLocalStatistics> getStatistics(LocalDate from, LocalDate to, String id) {
        List<CollectivityLocalStatistics> collectivityLocalStatisticsList = new ArrayList<>();
        String query = """
                    SELECT m.id AS member_id, m.first_name, m.last_name, m.email, m.member_occupation,
                    COALESCE(SUM(mp.amount), 0) AS earned_amount,
                    COALESCE(
                        (SELECT SUM(mf.amount)
                        FROM membership_fee mf
                        WHERE mf.status = 'ACTIVE'
                        AND mf.eligible_from BETWEEN ? AND ?
                        AND NOT EXISTS (
                            SELECT 1 FROM memberPayment mp2
                            WHERE mp2.id_member = m.id
                            AND mp2.id_membership_fee = mf.id
                            AND mp2.creation_date BETWEEN ? AND ?
                            )
                        ), 0
                    ) AS unpaid_amount
                    FROM member m LEFT JOIN memberPayment mp
                    ON mp.id_member = m.id
                    AND mp.creation_date BETWEEN ? AND ?
                    WHERE m.id_collectivity = ?
                    GROUP BY member_id, m.first_name, m.last_name, m.email, m.member_occupation
                    ORDER BY m.first_name, m.last_name;
                """;
        try (Connection connection = databaseConfig.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setDate(1, Date.valueOf(from));
            preparedStatement.setDate(2, Date.valueOf(to));
            preparedStatement.setDate(3, Date.valueOf(from));
            preparedStatement.setDate(4, Date.valueOf(to));
            preparedStatement.setDate(5, Date.valueOf(from));
            preparedStatement.setDate(6, Date.valueOf(to));
            preparedStatement.setString(7, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                CollectivityLocalStatistics collectivityLocalStatistics =  new CollectivityLocalStatistics();
                collectivityLocalStatistics.setId(resultSet.getString("member_id"));
                collectivityLocalStatistics.setFirstName(resultSet.getString("first_name"));
                collectivityLocalStatistics.setLastName(resultSet.getString("last_name"));
                collectivityLocalStatistics.setEmail(resultSet.getString("email"));
                String occupation = resultSet.getString("member_occupation");
                collectivityLocalStatistics.setOccupation(occupation == null ? null : Occupation.valueOf(occupation));
                collectivityLocalStatistics.setEarnedAmount(resultSet.getFloat("earned_amount"));
                collectivityLocalStatistics.setUnpaidAmount(resultSet.getFloat("unpaid_amount"));
                collectivityLocalStatisticsList.add(collectivityLocalStatistics);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return collectivityLocalStatisticsList;
    }
}
