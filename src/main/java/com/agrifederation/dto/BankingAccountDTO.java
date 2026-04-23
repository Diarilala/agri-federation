package com.agrifederation.dto;

import com.agrifederation.enums.MobileBankingService;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class BankingAccountDTO extends FinancialAccountDTO{
    private MobileBankingService mobileBankingService;
    private String accountNumber;

}
