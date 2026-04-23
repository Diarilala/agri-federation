package com.agrifederation.entity;

import com.agrifederation.enums.ActivityStatus;
import com.agrifederation.enums.Frequency;
import com.agrifederation.enums.PaymentMode;
import lombok.Data;

import java.time.LocalDate;

@Data
public class CollectivityTransaction {
    private String id;
    private LocalDate creationDate;
    private Double amount;
    private PaymentMode paymentMode;
    private String accountCreditedId;
    private FinancialAccount accountCredited;
    private String memberDebitedId;
    private Member memberDebited;
}
