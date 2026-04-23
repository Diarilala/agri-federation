package com.agrifederation.repository;

import com.agrifederation.config.DatabaseConfig;
import com.agrifederation.entity.BankAccount;
import com.agrifederation.entity.CashAccount;
import com.agrifederation.entity.FinancialAccount;
import com.agrifederation.entity.MobileBankingAccount;
import com.agrifederation.enums.MobileBankingService;
import lombok.Data;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@Repository
@Data
public class FinancialAccountRepository {
    private final DatabaseConfig databaseConfig;

    public FinancialAccount findById(String id) {
        CashAccount cashAccount = (CashAccount) findById(id);
        if (cashAccount != null) {
            return cashAccount;
        }

        BankAccount bankAccount = (BankAccount) findById(id);
        if (bankAccount != null) {
            return bankAccount;
        }

        MobileBankingAccount mobileBankingAccount = (MobileBankingAccount) findById(id);
        if (mobileBankingAccount != null) {
            return mobileBankingAccount;
        }

        return null;
    }

    public CashAccount findCashAccountById(String id) {
        String query = """
                SELECT ca.id, fa.amount
                FROM cash_account ca
                JOIN financial_account fa ON ca.id = fa.id
                WHERE ca.id = ?
                """;

        try (Connection connection = databaseConfig.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(query);) {
            preparedStatement.setString(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                CashAccount cashAccount = new CashAccount();
                cashAccount.setId(resultSet.getString("id"));
                cashAccount.setAmount(resultSet.getFloat(("amount")));
                return cashAccount;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    public BankAccount findBankAccountById(String id) {
        String query = """
                SELECT ba.id, ba.holder_name, ba.bank_name, ba.bank_code, 
                       ba.bank_branch_code, ba.bank_account_key, fa.amount
                FROM bank_account ba
                JOIN financial_account fa ON ba.id = fa.id
                WHERE ba.id = ?
        """;
        try (Connection connection = databaseConfig.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(query)){
            preparedStatement.setString(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                BankAccount bankAccount = new BankAccount();
                bankAccount.setId(resultSet.getString("id"));
                bankAccount.setHolderName(resultSet.getString("holder_name"));
                bankAccount.setBankCode(resultSet.getInt("bank_code"));
                bankAccount.setBankBranchCode(resultSet.getInt("bank_branch_code"));
                bankAccount.setBankAccountKey(resultSet.getInt("bank_account_key"));
                bankAccount.setAmount(resultSet.getFloat("amount"));
                return bankAccount;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    public MobileBankingAccount findMobileBankAccountById(String id) {
        String query = """
                SELECT mba.id, mba.holder_name, mba.mobile_banking_service, 
                       mba.mobile_number, fa.amount
                FROM mobile_banking_account mba
                JOIN financial_account fa ON mba.id = fa.id
                WHERE mba.id = ?
                """;
        try (Connection connection = databaseConfig.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(query)){
            preparedStatement.setString(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                MobileBankingAccount mobileBankingAccount = new MobileBankingAccount();
                mobileBankingAccount.setId(resultSet.getString("id"));
                mobileBankingAccount.setHolderName(resultSet.getString("holder_name"));
                mobileBankingAccount.setMobileBankingService(MobileBankingService.valueOf(resultSet.getString("mobile_banking_service")));
                mobileBankingAccount.setMobileNumber(resultSet.getString("mobile_number"));
                mobileBankingAccount.setAmount(resultSet.getFloat("amount"));
                return mobileBankingAccount;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;

    }





}
