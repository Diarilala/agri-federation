package com.agrifederation.repository;

import com.agrifederation.config.DatabaseConfig;
import lombok.Data;
import org.springframework.stereotype.Repository;

@Repository
@Data
public class FinancialAccountRepository {
    private final DatabaseConfig databaseConfig;
}
