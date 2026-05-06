package com.agrifederation.dto;

import com.agrifederation.enums.ActivityType;
import com.agrifederation.enums.Occupation;
import lombok.Data;

import java.util.List;

@Data
public class CreateCollectivityActivityDTO {
    private String label;
    private ActivityType activityType;
    private List<Occupation> memberOccupationConcerned;
    private MonthlyRecurrenceRuleDTO recurrenceRule;

}
