package com.agrifederation.repository;

import com.agrifederation.config.DatabaseConfig;
import com.agrifederation.entity.MemberPayment;
import com.agrifederation.service.CollectivityService;
import com.agrifederation.service.MemberPaymentService;
import lombok.Data;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Repository
@Data
public class MemberPaymentRepository {
    private final DatabaseConfig databaseConfig;
    private final CollectivityTransactionRepository collectivityTransactionRepository;
    private final CollectivityService collectivityService;
    private final MemberPaymentService memberPaymentService;

    public List<MemberPayment> createMemberPayments(List<MemberPayment> givenMemberPaymentList) {
        List<MemberPayment> memberPaymentList = new ArrayList<>();
        String memberPaymentQuery = """
                INSERT INTO memberPayment(id, amount, payment_mode,
                id_membership_fee, account_credited, creation_date
                ) VALUES (?, ?, ?::payment_mode, ?, ?, ?)
                RETURNING id;
                """;

        try (Connection connection = databaseConfig.getConnection()) {
            connection.setAutoCommit(false);
            try (PreparedStatement memberPaymentStmt = connection.prepareStatement(memberPaymentQuery)) {
                for (MemberPayment memberPayment : givenMemberPaymentList) {
                    memberPaymentStmt.setString(1, UUID.randomUUID().toString());
                    memberPaymentStmt.setFloat(2, memberPayment.getAmount());
                    memberPaymentStmt.setString(3, memberPayment.getPaymentMode().name());
                    memberPaymentStmt.setString(4, memberPayment.getMembershipFeeIdentifier());
                    memberPaymentStmt.setString(5, memberPayment.getAccountCreditedIdentifier());
                    memberPaymentStmt.setTimestamp(6, Timestamp.from(Instant.now()));

                    ResultSet resultSet = memberPaymentStmt.executeQuery();
                    if (resultSet.next()) {
                        String returnedId = resultSet.getString("id");
                        memberPayment.setId(returnedId);
                        memberPaymentList.add(memberPayment);
                    }
                }
                connection.commit();
            } catch (SQLException e) {
                connection.rollback();
                throw new RuntimeException(e);
            }
            return memberPaymentList;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
