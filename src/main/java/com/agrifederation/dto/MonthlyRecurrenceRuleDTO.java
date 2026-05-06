package com.agrifederation.dto;

import com.agrifederation.enums.dayOfWeek;
import lombok.Data;

@Data
public class MonthlyRecurrenceRuleDTO {
    private Integer weekOrdinal;
    private dayOfWeek dayOfWeek;
}
