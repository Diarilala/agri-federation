package com.agrifederation.repository;

import com.agrifederation.config.DatabaseConfig;
import com.agrifederation.entity.CollectivityLocalStatistics;
import com.agrifederation.enums.Occupation;
import lombok.Data;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.*;
import java.time.LocalDate;

@Repository
@Data
public class CollectivityLocalStatisticsRepository {
    private final DatabaseConfig databaseConfig;

    public CollectivityLocalStatistics getStatistics(LocalDate from, LocalDate to) {
        CollectivityLocalStatistics collectivityLocalStatistics = new CollectivityLocalStatistics();
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
                    GROUP BY member_id, m.first_name, m.last_name
                    ORDER BY m.first_name, m.last_name;
                """;
        try (Connection connection = databaseConfig.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, String.valueOf(from));
            preparedStatement.setString(2, String.valueOf(to));
            preparedStatement.setString(3, String.valueOf(from));
            preparedStatement.setString(4, String.valueOf(to));
            preparedStatement.setString(5, String.valueOf(from));
            preparedStatement.setString(6, String.valueOf(to));
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                collectivityLocalStatistics.setId(resultSet.getString("member_id"));
                collectivityLocalStatistics.setFirstName(resultSet.getString("first_name"));
                collectivityLocalStatistics.setLastName(resultSet.getString("last_name"));
                collectivityLocalStatistics.setEmail(resultSet.getString("email"));
                String occupation = resultSet.getString("member_occupation");
                collectivityLocalStatistics.setOccupation(occupation == null ? null : Occupation.valueOf(occupation));
                return collectivityLocalStatistics;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return collectivityLocalStatistics;
    }
}
