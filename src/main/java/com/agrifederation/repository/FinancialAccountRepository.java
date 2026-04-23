package com.agrifederation.repository;

import com.agrifederation.config.DatabaseConfig;
import com.agrifederation.entity.FinancialAccount;
import lombok.Data;
import org.springframework.stereotype.Repository;

import java.sql.SQLException;

@Repository
@Data
public class FinancialAccountRepository {
    private final DatabaseConfig databaseConfig;

    public FinancialAccountRepository getFinancialAccount(String id){

    }
}
