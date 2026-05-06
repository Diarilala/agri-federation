package com.agrifederation.repository;

import com.agrifederation.config.DatabaseConfig;
import com.agrifederation.dto.CollectivityInformationDTO;
import com.agrifederation.dto.CollectivityOverallStatisticsDTO;
import lombok.Data;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Repository
@Data
public class CollectivityOverallStatRepository {
    private final DatabaseConfig databaseConfig;
    private final CollectivityRepository collectivityRepository;

    public List<CollectivityOverallStatisticsDTO> findAllCollectivityOverallStats(String fromDate, String toDate) {
        List<CollectivityOverallStatisticsDTO> collectivityOverallStats = new ArrayList<>();

        String query = """
                SELECT 
                    c.unique_name as name,
                    c.unique_number as number,
                    COUNT(DISTINCT cm.member_id) as total_members,
                    COUNT(DISTINCT CASE WHEN cm.joined_at BETWEEN ?::date AND ?::date THEN cm.member_id END) as new_members,
                    COUNT(DISTINCT CASE WHEN mp.creation_date BETWEEN ?::date AND ?::date THEN mp.id_member END) as members_who_paid,
                    CASE
                        WHEN COUNT(DISTINCT cm.member_id) = 0 THEN 0
                        ELSE ROUND(
                            COUNT(DISTINCT CASE WHEN mp.creation_date BETWEEN ?::date AND ?::date THEN mp.id_member END) * 100.0
                            / COUNT(DISTINCT cm.member_id),
                            2
                        )
                    END as percentage
                FROM collectivity c
                LEFT JOIN collectivity_members cm ON cm.collectivity_id = c.id AND cm.is_active = true
                LEFT JOIN memberpayment mp ON mp.id_member = cm.member_id
                WHERE c.unique_name IS NOT NULL
                GROUP BY c.id, c.unique_name, c.unique_number
                ORDER BY c.unique_name;
                """;

        try(Connection connection = databaseConfig.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(query);
        ) {
            preparedStatement.setString(1, fromDate);
            preparedStatement.setString(2, toDate);
            preparedStatement.setString(3, fromDate);
            preparedStatement.setString(4, toDate);
            preparedStatement.setString(5, fromDate);
            preparedStatement.setString(6, toDate);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                CollectivityOverallStatisticsDTO dto = new CollectivityOverallStatisticsDTO();
                CollectivityInformationDTO infos = new CollectivityInformationDTO();
                infos.setName(resultSet.getString("name"));
                int number = resultSet.getInt("number");
                if (!resultSet.wasNull()) {
                    infos.setNumber(number);
                }
                dto.setCollectivityInformation(infos);
                dto.setMembersNumber(resultSet.getInt("new_members"));
                dto.setOverallMemberPercentage(resultSet.getDouble("percentage"));

                collectivityOverallStats.add(dto);
            }
        }catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return collectivityOverallStats;

    }
}
