package com.agrifederation.dto;

import com.agrifederation.entity.FinancialAccount;
import com.agrifederation.entity.Member;
import com.agrifederation.enums.PaymentMode;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class CollectivityTransactionDTO {
    private String id;
    private LocalDate creationDate;
    private Double amount;
    private PaymentMode paymentMode;
    private FinancialAccount accountCredited;
    private Member memberDebited;

}
