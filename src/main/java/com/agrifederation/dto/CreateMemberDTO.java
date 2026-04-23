package com.agrifederation.dto;

import com.agrifederation.enums.Gender;
import com.agrifederation.enums.Occupation;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class CreateMemberDTO {
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
    private List<String> referees;
    private boolean registrationFeePaid;
    private boolean membershipFeePaid;
}
