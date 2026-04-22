package com.agrifederation.repository;


import com.agrifederation.config.DatabaseConfig;
import com.agrifederation.entity.Collectivity;
import lombok.Data;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Repository
@Data
public class CollectivityRepository {
    private final DatabaseConfig databaseConfig;

    public List<Collectivity> createCollectivity(List<Collectivity> givenCollectivityList) {
        List<Collectivity> collectivityList = new ArrayList<>();
        String query = """
                INSERT INTO collectivity (id, location, specialty, federation_approval, approval_date, created_at)
                VALUES (?, ?, ?, ?, ?, ?)
                """;

        try (Connection connection = databaseConfig.getConnection()) {
            connection.setAutoCommit(false);
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            for (Collectivity collectivity : givenCollectivityList) {
                preparedStatement.setString(1, UUID.randomUUID().toString());
                preparedStatement.setString(2, collectivity.getLocation());
                preparedStatement.setString(3, collectivity.getSpeciality());
                preparedStatement.setString(4, collectivity.getFederationApproval().booleanValue() ? "Y" : "N");
                preparedStatement.setString(5, collectivity.getApprovalDate().toString());
                preparedStatement.setString(6, collectivity.getCreatedAt().toString());
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return collectivityList;
    }

    //check if collectivity exists
    public boolean CollectivityExists(String collectivityIdentifier) {
        String query = """
                SELECT id
                FROM collectivity
                WHERE id = ?
        """;
        try (Connection connection = databaseConfig.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(query);){
            preparedStatement.setString(1, collectivityIdentifier);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return true;
            }
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Collectivity findById(Integer id) {
        String query = """
                SELECT id, location, specialty, federation_approval, approval_date, created_at
                FROM collectivity
                WHERE id = ?
                """;
        try(Connection connection = databaseConfig.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(query)){
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                Collectivity collectivity = new Collectivity();
                collectivity.setId(resultSet.getString("id"));
                collectivity.setLocation(resultSet.getString("location"));
                collectivity.setSpeciality(resultSet.getString("specialty"));
                collectivity.setFederationApproval(resultSet.getBoolean("federation_approval"));
                collectivity.setApprovalDate(resultSet.getTimestamp("approval_date").toLocalDateTime());
                collectivity.setCreatedAt(resultSet.getTimestamp("created_at").toLocalDateTime());
                return collectivity;

            }
    }catch (SQLException e) {
        throw new RuntimeException(e);}
        return null;
    }

    public List<Collectivity> findAll() {
        List<Collectivity> collectivityList = new ArrayList<>();
        String query = """
                SELECT id, location, specialty, federation_approval, approval_date, created_at
                FROM collectivity
                ORDER BY id
        """;
        try(Connection connection = databaseConfig.getConnection();
        PreparedStatement statement = connection.prepareStatement(query)){
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Collectivity collectivity = new Collectivity();
                collectivity.setId(resultSet.getString("id"));
                collectivity.setLocation(resultSet.getString("location"));
                collectivity.setSpeciality(resultSet.getString("specialty"));
                collectivity.setFederationApproval(resultSet.getBoolean("federation_approval"));
                collectivity.setApprovalDate(resultSet.getTimestamp("approval_date").toLocalDateTime());
                collectivity.setCreatedAt(resultSet.getTimestamp("created_at").toLocalDateTime());
                collectivityList.add(collectivity);
            }
    }catch (SQLException e) {
        throw new RuntimeException(e);}
    return collectivityList;
    }
    }

