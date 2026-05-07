package com.agrifederation.dto;

import com.agrifederation.enums.Occupation;
import lombok.Data;

@Data
public class MemberDescriptionDTO {
    private String id;
    private String firstName;
    private String lastName;
    private String email;
    private Occupation occupation;
}
