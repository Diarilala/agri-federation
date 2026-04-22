package com.agrifederation.entity;

import com.agrifederation.enums.Gender;
import com.agrifederation.enums.Occupation;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor

public class Member {
    private String memberIdentifier;
    private String firstName;
    private String lastName;
    private LocalDate birthDate;
    private Gender gender;
    private String address;
    private String profession;
    private int phoneNumber;
    private String email;
    private Occupation occupation;
    private String collectivityIdentifier;
    private List<Referral> referrals;
    private boolean registrationFeePaid;
    private boolean membershipDuesPaid;
    private List<MemberPayment> memberPayments;
}