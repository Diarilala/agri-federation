package com.agrifederation.entity;

import com.agrifederation.enums.PaymentMode;
import lombok.Data;

import java.time.Instant;

@Data
public class MemberPayment {
    private String id;
    private int amount;
    private PaymentMode paymentMode;
    private String membershipFeeIdentifier;
    private String accountCreditedIdentifier;
    private FinancialAccount accountCredited;
    private Instant creationDate;
}
