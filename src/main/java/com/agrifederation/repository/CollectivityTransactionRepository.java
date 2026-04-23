package com.agrifederation.repository;

import com.agrifederation.config.DatabaseConfig;
import com.agrifederation.entity.CollectivityTransaction;
import com.agrifederation.enums.PaymentMode;
import lombok.Data;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Repository
@Data
public class CollectivityTransactionRepository {
    private final DatabaseConfig databaseConfig;

    public List<CollectivityTransaction> findCollectivityTransaction(String collectivityId, LocalDate fromDate, LocalDate toDate, LocalDate dateFrom, LocalDate dateTo) {
        List<CollectivityTransaction> collectivityTransactions = new ArrayList<>();
        String query = """
                SELECT ct.id,
                    ct.creation_date,
                    ct.amount,
                    ct.payment_mode,
                    ct.account_credited_id,
                    ct.member_debited_id,
                    m.id as member_id,
                    m.first_name,
                    m.last_name,
                    m.birth_date,
                    m.gender,
                    m.address,
                    m.profession,
                    m.phone_number,
                    m.email,
                    m.member_occupation
                FROM collectivity_transaction ct
                LEFT JOIN member m ON ct.member_debited_id = m.id
                WHERE ct.collectivity_id = ?
                    AND ct.creation_date >= ?
                    AND ct.creation_date <= ?
                ORDER BY ct.creation_date DESC
                """;
        try (Connection connection = databaseConfig.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)){
            preparedStatement.setString(1, collectivityId);
            preparedStatement.setDate(2, Date.valueOf(fromDate));
            preparedStatement.setDate(3, Date.valueOf(toDate));
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                CollectivityTransaction collectivityTransaction = new CollectivityTransaction();
                collectivityTransaction.setId(resultSet.getString("id"));
                collectivityTransaction.setCreationDate(resultSet.getDate("creation_date").toLocalDate());
                collectivityTransaction.setAmount(resultSet.getDouble("amount"));
                collectivityTransaction.setPaymentMode(PaymentMode.valueOf(resultSet.getString("payment_mode")));
                collectivityTransaction.setAccountCreditedId(resultSet.getString("account_credited_id"));
                collectivityTransaction.setMemberDebitedId(resultSet.getString("member_debited_id"));

                collectivityTransactions.add(collectivityTransaction);

            }

        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return collectivityTransactions;
    }

    public CollectivityTransaction createCollectivityTransaction(CollectivityTransaction collectivityTransaction, String collectivityId) {
        String query = """
                INSERT INTO collectivity_transaction (id, creation_date, amount, payment_mode, account_credited_id, member_debited_id, collectivity_id)
                VALUES (?, ?, ?, ?::payment_mode, ?, ?, ?)
                RETURNING id, creation_date, amount, payment_mode, account_credited_id, member_debited_id
                """;

        try (Connection connection = databaseConfig.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(query)){
            preparedStatement.setString(1, java.util.UUID.randomUUID().toString());
            preparedStatement.setDate(2, Date.valueOf(collectivityTransaction.getCreationDate()));
            preparedStatement.setDouble(3, collectivityTransaction.getAmount());
            preparedStatement.setString(4, collectivityTransaction.getPaymentMode().name());
            preparedStatement.setString(5, collectivityTransaction.getAccountCreditedId());
            preparedStatement.setString(6, collectivityTransaction.getMemberDebitedId());
            preparedStatement.setString(7, collectivityTransaction.getId());
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                CollectivityTransaction CreateTransaction = new CollectivityTransaction();
                CreateTransaction.setId(resultSet.getString("id"));
                CreateTransaction.setCreationDate(resultSet.getDate("creation_date").toLocalDate());
                CreateTransaction.setAmount(resultSet.getDouble("amount"));
                CreateTransaction.setPaymentMode(PaymentMode.valueOf(resultSet.getString("payment_mode")));
                CreateTransaction.setAccountCreditedId(resultSet.getString("account_credited_id"));
                return CreateTransaction;
            }


        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return null;
    }

    }
