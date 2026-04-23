package com.agrifederation.entity;

import com.agrifederation.enums.ActivityStatus;
import com.agrifederation.enums.Frequency;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CollectivityTransaction {
    private String id;
    private ActivityStatus status;
    private LocalDateTime eligibleDate;
    private Frequency frequency;
    private Double amount;
    private String description;
    private String collectivityId;
}
