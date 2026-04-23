package com.agrifederation.controller;

import com.agrifederation.dto.MemberPaymentDTO;
import com.agrifederation.entity.CollectivityTransaction;
import com.agrifederation.entity.MemberPayment;
import com.agrifederation.exception.BadRequestException;
import com.agrifederation.service.MemberPaymentService;
import com.agrifederation.service.MemberService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
public class MemberPaymentController {
    private final MemberPaymentService memberPaymentService;

    @PostMapping("/members/{id}/payments")
    public ResponseEntity<?> createPayments(@RequestBody List<MemberPayment> givenMemberPaymentList, @PathVariable String id) {
        List<MemberPaymentDTO> paymentList;
        CollectivityTransaction collectivityTransaction = new CollectivityTransaction();
        try {
            paymentList = memberPaymentService.createPayments(givenMemberPaymentList, id);
        } catch (BadRequestException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(paymentList);
    }
}
