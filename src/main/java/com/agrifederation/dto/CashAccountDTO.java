package com.agrifederation.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class CashAccountDTO extends FinancialAccountDTO {
    private String location;
}
