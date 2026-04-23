package com.agrifederation.controller;

import com.agrifederation.entity.Member;
import com.agrifederation.exception.BadRequestException;
import com.agrifederation.service.MemberService;
import com.agrifederation.validator.MemberValidator;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/members")
public class MemberController {

    private final MemberService memberService;
    private final MemberValidator memberValidator;

    @PostMapping
    public ResponseEntity<?> createMembers(@RequestBody List<Member> givenMemberList) {
        List<Member> memberList;
        try {
            memberValidator.validateMemberList(givenMemberList);
            memberList = memberService.createMembers(givenMemberList);
        } catch(BadRequestException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch(Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(memberList);
    }
}
