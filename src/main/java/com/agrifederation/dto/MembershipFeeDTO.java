package com.agrifederation.dto;

import com.agrifederation.enums.ActivityStatus;
import com.agrifederation.enums.Frequency;
import lombok.Data;

import java.time.LocalDate;

@Data
public class MembershipFeeDTO {
     private String id;
    private ActivityStatus status;
    private LocalDate eligibleFrom;
    private Frequency frequency;
    private Double amount;
    private String label;
}
