package com.agrifederation.entity;

import com.agrifederation.enums.BankName;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=true)
public class BankAccount extends FinancialAccount{
    private String holderName;
    private BankName bankName;
    private int bankCode;
    private int bankBranchCode;
    private int bankAccountKey;
    private float amount;
}
