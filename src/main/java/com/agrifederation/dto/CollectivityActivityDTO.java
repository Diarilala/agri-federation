package com.agrifederation.dto;

import com.agrifederation.enums.ActivityType;
import com.agrifederation.enums.Occupation;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class CollectivityActivityDTO {
    private String id;
    private String label;
    private ActivityType activityType;
    private List<Occupation> occupation;
    private MonthlyRecurrenceRuleDTO recurrenceRule;
    private LocalDate executiveDate;
}
