package com.agrifederation.entity;

import com.agrifederation.enums.DaysEnum;
import lombok.Data;

@Data
public class MonthlyRecurrenceRule {
    private int weekOrdinal;
    private DaysEnum dayOfWeek;
}
