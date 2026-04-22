package com.agrifederation.controller;

import com.agrifederation.entity.Member;
import com.agrifederation.exception.BadRequestException;
import com.agrifederation.repository.MemberRepository;
import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Data
@RestController
@RequestMapping("/members")
public class MemberController {

    private final MemberRepository memberRepository;

    @PostMapping
    public ResponseEntity<?> createMembers(@RequestBody List<Member> givenMemberList) {
        List<Member> memberList;
        try {
            memberList = memberRepository.createMembers(givenMemberList);
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
