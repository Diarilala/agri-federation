package com.agrifederation.config;

import org.springframework.context.annotation.Configuration;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

@Configuration
public class DatabaseConfig {
    public Connection getConnection() {
        try {
            String url = System.getenv("SPRING_DATASOURCE_URL");
            String username = System.getenv("SPRING_DATASOURCE_USERNAME");
            String password = System.getenv("SPRING_DATASOURCE_PASSWORD");
            return DriverManager.getConnection("jdbc:postgresql://localhost:5432/agri_federation", "postgres", "lavorary");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
