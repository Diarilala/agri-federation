package com.agrifederation.dto;

import com.agrifederation.enums.DayOfWeek;
import lombok.Data;

@Data
public class MonthlyRecurrenceRuleDTO {
    private Integer weekOrdinal;
    private DayOfWeek dayOfWeek;
}
