package com.agrifederation.service;

import com.agrifederation.entity.Member;
import com.agrifederation.exception.BadRequestException;
import com.agrifederation.exception.NotFoundException;
import com.agrifederation.repository.MemberRepository;
import lombok.Data;
import org.springframework.stereotype.Service;

import java.util.List;

@Data
@Service
public class MemberService {
    private MemberRepository memberRepository;
    private CollectivityRepository collectivityRepository;

    public List<Member> createMembers(List<Member> givenMemberList) {
        for(Member member : givenMemberList) {
            if(!collectivityRepository.collectivityExists(member.getCollectivityIdentifier())) {
                throw new NotFoundException("Collectivity Not Found: " + member.getCollectivityIdentifier());
            }
            if(!memberRepository.memberExists(member.getId())){
                throw new NotFoundException("Member Not Found: " + member.getId());
            }
            if(!member.isRegistrationFeePaid()) {
                throw new BadRequestException("Member Not Found: " + member.getId());
            }
            if (!member.isMembershipDuesPaid()) {
                throw new BadRequestException("Member Not Found: " + member.getId());
            }
        }
        return memberRepository.createMembers(givenMemberList);
    }
}
