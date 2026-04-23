package com.agrifederation.dto;

import com.agrifederation.enums.PaymentMode;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CollectivityTransactionDTO {
    private String id;
    private LocalDateTime creationDate;
    private Double amount;
    private PaymentMode paymentMode;

}
