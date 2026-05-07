package com.agrifederation.entity;

import com.agrifederation.enums.DaysEnum;
import lombok.Data;

@Data
public class MonthlyRecurrenceRule {
    private Integer weekOrdinal;
    private DaysEnum dayOfWeek;
}
