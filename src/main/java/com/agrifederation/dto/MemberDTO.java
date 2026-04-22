package com.agrifederation.dto;

import com.agrifederation.entity.Referral;
import com.agrifederation.enums.Gender;
import com.agrifederation.enums.Occupation;

import java.time.LocalDate;
import java.util.List;

public class MemberDTO {
    private String id;
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
}
