package com.agrifederation.service;

import com.agrifederation.dto.MemberPaymentDTO;
import com.agrifederation.entity.MemberPayment;
import com.agrifederation.repository.CollectivityTransactionRepository;
import com.agrifederation.repository.MemberPaymentRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class MemberPaymentService {
    private final MemberPaymentRepository memberPaymentRepository;
    private final CollectivityTransactionRepository collectivityTransactionRepository;

    public List<MemberPaymentDTO> createPayments(List<MemberPayment> givenMemberPaymentList, String memberIdentifier) {
        List<MemberPaymentDTO> memberPaymentDTOList = new ArrayList<>();
        List<MemberPayment> memberPaymentList = memberPaymentRepository.createMemberPayments(givenMemberPaymentList);
        if(!memberPayment)
        return memberPaymentDTOList;
    }
}
