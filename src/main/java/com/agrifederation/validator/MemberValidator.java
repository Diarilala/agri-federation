package com.agrifederation.validator;

import com.agrifederation.entity.Member;
import com.agrifederation.exception.BadRequestException;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class MemberValidator {
    public void validateMember(Member member) {
        String message ="";
        if(member.getFirstName() == null || member.getFirstName().isBlank()){
            message += "First Name is Required";
        }
        if(member.getLastName() == null || member.getLastName().isBlank()){
            message += "Last Name is Required";
        }
        if(member.getEmail() == null || member.getEmail().isBlank()){
            message += "Email is Required";
        }
        if(member.getCollectivityIdentifier() == null || member.getCollectivityIdentifier().isBlank()){
            message += "Collectivity Identifier is Required";
        }
        if(member.getReferrals() == null || member.getReferrals().isEmpty()){
            message += "Referrals is Required";
        }
        if(member.getAddress() == null || member.getAddress().isBlank()){
            message += "Address is Required";
        }
        if(member.getBirthDate() == null || member.getBirthDate().toString().isEmpty()){
            message += "Birth Date is Required";
        }
        if(member.getGender() == null || member.getGender().name().isEmpty()){
            message += "Gender is Required";
        }
        if(member.getCollectivityIdentifier() == null || member.getCollectivityIdentifier().isBlank()){
            message += "Collectivity Identifier is Required";
        }
        if(member.getOccupation() == null || member.getOccupation().name().isEmpty()){
            message += "Occupation is Required";
        }
        if(member.getProfession() == null || member.getProfession().isBlank()){
            message += "Profession is Required";
        }
        if(!message.isEmpty()){
            throw new BadRequestException(message);
        }
    }

    public void validateMemberList(List<Member> givenMemberList) {
        if(givenMemberList.isEmpty() || givenMemberList == null){
            throw new BadRequestException("Given Members List is Empty");
        }
        for(Member member : givenMemberList){
            validateMember(member);
        }
    }
}
