package com.agrifederation.repository;


import com.agrifederation.config.DatabaseConfig;
import com.agrifederation.entity.MembershipFee;
import com.agrifederation.enums.ActivityStatus;
import com.agrifederation.enums.Frequency;
import lombok.Data;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Repository
@Data
public class MembershipFeeRepository {
    private final DatabaseConfig databaseConfig;

    public List<MembershipFee> findByCollectivityId(String collectivityId) {
        List<MembershipFee> membershipFees = new ArrayList<>();
        String query = """
                SELECT id, status, eligible_from, frequency, amount, label, collectivity_id
                FROM membership_fee
                WHERE collectivity_id = ?
                ORDER BY eligible_from DESC
                """;

        try (Connection con = databaseConfig.getConnection();
             PreparedStatement preparedStatement = con.prepareStatement(query);) {
            preparedStatement.setString(1, collectivityId);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                MembershipFee membershipFee = new MembershipFee();
                membershipFee.setId(resultSet.getString("id"));
                membershipFee.setStatus(ActivityStatus.valueOf(resultSet.getString("status")));
                membershipFee.setFrequency(Frequency.valueOf(resultSet.getString("frequency")));
                membershipFee.setAmount(resultSet.getFloat(resultSet.getString("amount")));
                membershipFee.setLabel(resultSet.getString("label"));
                membershipFees.add(membershipFee);

            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return membershipFees;
    }

    public List<MembershipFee> createMembershipFee(String collectivityId, List<MembershipFee> membershipFees) {
        List<MembershipFee> createMembershipFee = new ArrayList<>();
        String query = """
                INSERT INTO membership_fee (id, status, eligible_from, frequency, amount, label, collectivity_id)
                VALUES (?, ?::activity_status, ?, ?::frequency, ?, ?, ?)
                RETURNING id, status, eligible_from, frequency, amount, label, collectivity_id
        """;

        try (Connection connection = databaseConfig.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query);
        ) {
            connection.setAutoCommit(false);
            for (MembershipFee membershipFee : membershipFees) {
                String id = UUID.randomUUID().toString();
                preparedStatement.setString(1, id);
                preparedStatement.setString(2, membershipFee.getStatus().name());
                preparedStatement.setFloat(3, membershipFee.getFrequency().ordinal());
                preparedStatement.setFloat(4, membershipFee.getAmount());
                preparedStatement.setString(5, membershipFee.getLabel());
                ResultSet resultSet = preparedStatement.executeQuery();
                while (resultSet.next()) {
                    MembershipFee createdMembershipFee = new MembershipFee();
                    createdMembershipFee.setId(resultSet.getString("id"));
                    createdMembershipFee.setStatus(ActivityStatus.valueOf(resultSet.getString("status")));
                    createdMembershipFee.setFrequency(Frequency.valueOf(resultSet.getString("frequency")));
                    createdMembershipFee.setAmount(resultSet.getFloat("amount"));
                    createdMembershipFee.setLabel(resultSet.getString("label"));
                    createMembershipFee.add(createdMembershipFee);

                }
                connection.commit();
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return createMembershipFee;
    }

    public boolean existsById(String id) {
        String query = """
                SELECT 1 FROM membership_fee
                WHERE id = ?
        """;

        try (Connection connection = databaseConfig.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(query)){
            preparedStatement.setString(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            return resultSet.next();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public MembershipFee findById(String id) {
        String query = """
                SELECT id, status, eligible_from, frequency, amount, label, collectivity_id
                FROM membership_fee
                WHERE id = ?
        """;

        try (Connection connection = databaseConfig.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(query)){
            preparedStatement.setString(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                MembershipFee membershipFee = new MembershipFee();
                membershipFee.setId(resultSet.getString("id"));
                membershipFee.setStatus(ActivityStatus.valueOf(resultSet.getString("status")));
                membershipFee.setFrequency(Frequency.valueOf(resultSet.getString("frequency")));
                membershipFee.setAmount(resultSet.getFloat("amount"));
                membershipFee.setLabel(resultSet.getString("label"));
                return membershipFee;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }
}