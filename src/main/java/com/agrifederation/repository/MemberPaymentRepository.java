package com.agrifederation.repository;

import com.agrifederation.config.DatabaseConfig;
import com.agrifederation.entity.MemberPayment;
import lombok.Data;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Repository
@Data
public class MemberPaymentRepository {
    private final DatabaseConfig databaseConfig;

    public List<MemberPayment> createMemberPayments(List<MemberPayment> givenMemberPaymentList) {
        List<MemberPayment> memberPaymentList = new ArrayList<>();
        String memberPaymentQuery = """
                INSERT INTO memberPayment(id, amount, payment_mode,
                id_membership_fee, account_credited, creation_date,
                id_member) VALUES (?, ?, ?::payment_mode, ?, ?, ?, ?)
                RETURNING id;
                """;

        String createCollectivityTransaction = """
                INSERT INTO
                """
        try (Connection connection = databaseConfig.getConnection()) {
            connection.setAutoCommit(false);
            try (PreparedStatement memberPaymentStmt = connection.prepareStatement(memberPaymentQuery)){
                for (MemberPayment memberPayment : givenMemberPaymentList) {
                    String paymentMode =
                    memberPaymentStmt.setString(1, UUID.randomUUID().toString());
                    memberPaymentStmt.setFloat(2, memberPayment.getAmount());
                    memberPaymentStmt.setString(3, memberPayment.getPaymentMode().name());
                    memberPaymentStmt.setString(4, memberPayment.getMembershipFeeIdentifier());
                    memberPaymentStmt.setString(5, memberPayment.getAccountCreditedIdentifier());
                    memberPaymentStmt.setTimestamp(6, Timestamp.from(Instant.now()));
                    memberPaymentStmt.setString(7, memberPayment.getMember().getMemberIdentifier());

                    ResultSet resultSet = memberPaymentStmt.executeQuery();
                    if(resultSet.next()) {

                    }
                }
            }
        }
    }
}
