package com.agrifederation.dto;

import com.agrifederation.enums.PaymentMode;
import lombok.Data;

@Data
public class FinancialAccountDTO {
    private String id;
    private PaymentMode type;
    private Double amount;
    private String holderName;
    private String details;
}
