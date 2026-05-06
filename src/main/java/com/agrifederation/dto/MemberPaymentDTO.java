package com.agrifederation.dto;

import com.agrifederation.entity.FinancialAccount;
import com.agrifederation.entity.MembershipFee;
import com.agrifederation.enums.PaymentMode;

import java.time.Instant;

public class MemberPaymentDTO {
    private String id;
    private float amount;
    private PaymentMode paymentMode;
    private String membershipFeeIdentifier;
    private String accountCreditedIdentifier;
    private FinancialAccount accountCredited;
    private MembershipFee membershipFee;
    private Instant creationDate;
}
