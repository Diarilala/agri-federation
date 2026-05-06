package com.agrifederation.entity;

import com.agrifederation.enums.Occupation;
import com.agrifederation.enums.Type;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
public class CollectivityActivity {
    private String id;
    private String label;
    private Type activityType;
    private String idCollectivity;
    private String idMonthlyRecurrence;
    private List<Occupation> memberOccupationConcerned;
    private MonthlyRecurrenceRule monthlyRecurrenceRule;
    private LocalDate executiveDate;
}
