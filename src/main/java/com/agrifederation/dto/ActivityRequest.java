package com.agrifederation.dto;

import com.agrifederation.entity.MonthlyRecurrenceRule;
import com.agrifederation.enums.Occupation;
import com.agrifederation.enums.Type;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class ActivityRequest {
    private String label;
    private Type activityType;
    private String idCollectivity;
    private String idMonthlyRecurrence;
    private List<Occupation> memberOccupationConcerned;
    private MonthlyRecurrenceRule monthlyRecurrenceRule;
    private LocalDate executiveDate;
}
