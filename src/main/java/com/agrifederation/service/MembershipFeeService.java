package com.agrifederation.service;

import com.agrifederation.dto.CreateMembershipFeeDTO;
import com.agrifederation.dto.MembershipFeeDTO;
import com.agrifederation.entity.MembershipFee;
import com.agrifederation.exception.NotFoundException;
import com.agrifederation.repository.CollectivityRepository;
import com.agrifederation.repository.MembershipFeeRepository;
import lombok.Data;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Data
public class MembershipFeeService {
    private final MembershipFeeRepository membershipFeeRepository;
    private final CollectivityService collectivityService;
    private final CollectivityRepository collectivityRepository;

    public List<MembershipFeeDTO> getMembershipFees(String collectivityId) {
        if (!collectivityRepository.CollectivityExists(collectivityId)) {
            throw new NotFoundException("Collectivity with id " + collectivityId + " not found");
        }

        List<MembershipFee> membershipFees = membershipFeeRepository.findByCollectivityId(collectivityId);
        List<MembershipFeeDTO> membershipFeeDTOS = new ArrayList<>();
        for (MembershipFee membershipFee : membershipFees) {
            MembershipFeeDTO membershipFeeDTO = new MembershipFeeDTO();
            membershipFeeDTO.setId(membershipFee.getId());
            membershipFeeDTO.setStatus(membershipFee.getStatus());
            membershipFeeDTO.setEligibleFrom(membershipFee.getEligibleFrom());
            membershipFeeDTO.setFrequency(membershipFee.getFrequency());
            membershipFeeDTO.setAmount(Double.valueOf(membershipFee.getAmount()));
            membershipFeeDTO.setLabel(membershipFee.getLabel());
            membershipFeeDTOS.add(membershipFeeDTO);
        }
        return membershipFeeDTOS;
    }

    public List<MembershipFeeDTO> createMembershipFees(String collectivityId, List<CreateMembershipFeeDTO> createDTOs) {
        if (!collectivityRepository.CollectivityExists(collectivityId)) {
            throw new NotFoundException("Collectivity with id " + collectivityId + " not found");

        }
        List<MembershipFee> membershipFees = new ArrayList<>();
        for (CreateMembershipFeeDTO createMembershipFeeDTO : createDTOs) {
            MembershipFee membershipFee = new MembershipFee();
            membershipFee.setEligibleFrom(createMembershipFeeDTO.getEligibleFrom());
            membershipFee.setFrequency(createMembershipFeeDTO.getFrequency());
            membershipFee.setAmount(Float.valueOf(String.valueOf(createMembershipFeeDTO.getAmount())));
            membershipFee.setLabel(createMembershipFeeDTO.getLabel());
            membershipFees.add(membershipFee);
        }
        List<MembershipFeeDTO> membershipFeeDTOS = new ArrayList<>();
        for (MembershipFee membershipFee : membershipFees) {
            MembershipFeeDTO membershipFeeDTO = new MembershipFeeDTO();
            membershipFeeDTO.setId(membershipFee.getId());
            membershipFeeDTO.setStatus(membershipFee.getStatus());
            membershipFeeDTO.setEligibleFrom(membershipFee.getEligibleFrom());
            membershipFeeDTO.setFrequency(membershipFee.getFrequency());
            membershipFeeDTO.setAmount(Double.valueOf(membershipFee.getAmount()));
            membershipFeeDTO.setLabel(membershipFee.getLabel());
            membershipFeeDTOS.add(membershipFeeDTO);
        }
        return membershipFeeDTOS;
    }
}
