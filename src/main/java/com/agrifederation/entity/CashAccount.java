package com.agrifederation.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=true)
public class CashAccount extends FinancialAccount {
    private float amount;s
}
