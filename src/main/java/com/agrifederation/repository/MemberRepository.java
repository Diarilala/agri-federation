package com.agrifederation.repository;

import com.agrifederation.config.DatabaseConfig;
import com.agrifederation.entity.Member;
import lombok.Data;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.UUID;

@Repository
@Data

public class MemberRepository {
    private final DatabaseConfig databaseConfig;

    public List<Member> createMembers(List<Member> givenMembersList) {
        List<Member> members = new ArrayList<>();
        String query = """
                INSERT INTO member (id, first_name, last_name,
                                     birth_date, gender, address,
                                     profession, phone_number, email,
                                     member_occupation) VALUES (?, ?, ?, ?, ?::gender, ?, ?, ?, ?, ?::member_occupation)
                """;

        try (Connection connection = databaseConfig.getConnection()) {
            connection.setAutoCommit(false);
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            for(Member member : givenMembersList) {
                preparedStatement.setString(1, UUID.randomUUID().toString());
                preparedStatement.setString(2, member.getFirstName());
                preparedStatement.setString(3, member.getLastName());
                preparedStatement.setString(4, member.getBirthDate().toString());
                preparedStatement.setString(5, member.getGender().name());
                preparedStatement.setString(6, member.getAddress());
                preparedStatement.setString(7, member.getProfession());
                preparedStatement.setInt(8, member.getPhoneNumber());
                preparedStatement.setString(9, member.getEmail());
                preparedStatement.setString(10, member.getOccupation().name());

            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return members;
    }

}
