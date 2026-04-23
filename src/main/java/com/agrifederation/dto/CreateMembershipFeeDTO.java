package com.agrifederation.dto;

import com.agrifederation.enums.Frequency;
import lombok.Data;

import java.time.LocalDate;

@Data
public class CreateMembershipFeeDTO {
     private LocalDate eligibleFrom;
    private Frequency frequency;
    private Double amount;
    private String label;
}
