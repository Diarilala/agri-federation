package com.agrifederation.service;

import com.agrifederation.entity.Member;
import com.agrifederation.exception.BadRequestException;
import com.agrifederation.exception.NotFoundException;
import com.agrifederation.repository.CollectivityRepository;
import com.agrifederation.repository.MemberRepository;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.stereotype.Service;

import java.util.List;

@Data
@AllArgsConstructor
@Service
public class MemberService {
    private MemberRepository memberRepository;
    private CollectivityRepository collectivityRepository;

    public List<Member> createMembers(List<Member> givenMemberList) {
        for(Member member : givenMemberList) {
            if(!member.isRegistrationFeePaid()) {
                throw new BadRequestException("Registration Fee Not Paid");
            }
            if (!member.isMembershipDuesPaid()) {
                throw new BadRequestException("Membership Dues Not Paid");
            }
        }
        return memberRepository.createMembers(givenMemberList);
    }
}
