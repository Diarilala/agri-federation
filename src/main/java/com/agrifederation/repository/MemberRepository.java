package com.agrifederation.repository;

import com.agrifederation.config.DatabaseConfig;
import com.agrifederation.entity.Member;
import com.agrifederation.entity.Referral;
import lombok.Data;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
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
        String memberQuery = """
                INSERT INTO member (id, first_name, last_name,
                                     birth_date, gender, address,
                                     profession, phone_number, email,
                                     member_occupation, id_collectivity, registration_fee_paid,
                                    membership_dues_paid) VALUES (?, ?, ?, ?, ?::gender_type, ?, ?, ?, ?, ?::occupation_type,?, ?, ?)
                                    RETURNING id;
                """;

        String referralQuery = """
                INSERT INTO referral (id, id_referee, id_referred)
                VALUES (?, ?, ?)
                """;

        try (Connection connection = databaseConfig.getConnection()) {
            connection.setAutoCommit(false);
            try (PreparedStatement memberStmt = connection.prepareStatement(memberQuery);
                 PreparedStatement referralStmt = connection.prepareStatement(referralQuery);) {
                for (Member member : givenMembersList) {
                    memberStmt.setString(1, UUID.randomUUID().toString());
                    memberStmt.setString(2, member.getFirstName());
                    memberStmt.setString(3, member.getLastName());
                    memberStmt.setString(4, member.getBirthDate().toString());
                    memberStmt.setString(5, member.getGender().name());
                    memberStmt.setString(6, member.getAddress());
                    memberStmt.setString(7, member.getProfession());
                    memberStmt.setInt(8, member.getPhoneNumber());
                    memberStmt.setString(9, member.getEmail());
                    memberStmt.setString(10, member.getOccupation().name());
                    memberStmt.setString(11, member.getCollectivityIdentifier());
                    memberStmt.setBoolean(12, member.isRegistrationFeePaid());
                    memberStmt.setBoolean(13, member.isMembershipDuesPaid());

                    ResultSet rs = memberStmt.executeQuery();
                    if(rs.next()) {
                        String returnedId = rs.getString("id");
                        member.setId(returnedId);
                        members.add(member);

                        if(member.getReferrals() != null || !member.getReferrals().isEmpty()){
                            for(Referral referral : member.getReferrals()){
                                referralStmt.setString(1, UUID.randomUUID().toString());
                                referralStmt.setString(2, referral.getReferee().getId());
                                referralStmt.setString(3, returnedId);
                                referralStmt.addBatch();
                            }
                        }
                    }
                }
                referralStmt.executeBatch();
                connection.commit();
            } catch (SQLException e) {
                connection.rollback();
                throw new RuntimeException(e);
            }
            return members;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean memberExists(String memberId) {
        String query = """
                SELECT id FROM member WHERE id = ?
        """;
        try (Connection connection = databaseConfig.getConnection();
        PreparedStatement stmt = connection.prepareStatement(query)){
            stmt.setString(1, memberId);
            ResultSet resultSet = stmt.executeQuery();
            return resultSet.next();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


}
