package com.agrifederation.entity;

import com.agrifederation.enums.ActivityStatus;
import com.agrifederation.enums.Frequency;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class MembershipFee {
    private String id;
    private ActivityStatus status;
    private LocalDate eligibleFrom;
    private Frequency frequency;
    private float amount;
    private String label;
    private List<MemberPayment> memberPayments;
}
