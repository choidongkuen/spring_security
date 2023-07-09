package com.example.spring_security.controller;

import com.example.spring_security.domain.jwt.TokenInfo;
import com.example.spring_security.dto.GetMemberInfoResponseDto;
import com.example.spring_security.dto.MemberLoginRequestDto;
import com.example.spring_security.dto.MemberSignUpRequestDto;
import com.example.spring_security.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Slf4j
@RequiredArgsConstructor
@RestController
public class MemberController {

    private final MemberService memberService;

    @Value("${spring.datasource.password}")
    private Long password;

    @PostMapping("/member/join")
    public ResponseEntity<Long> signUp(
            @Valid @RequestBody MemberSignUpRequestDto request
    ) {
        return new ResponseEntity<>(this.memberService.signUp(request), HttpStatus.OK);
    }

    @GetMapping("/members/login")
    public ResponseEntity<TokenInfo> login(
            @Valid @RequestBody MemberLoginRequestDto request
    ) {
        return new ResponseEntity<>(this.memberService.login(request), HttpStatus.OK);
    }

    @GetMapping("/members/{memberId}/info")
    public ResponseEntity<GetMemberInfoResponseDto> getMemberInfo(
            @PathVariable(value = "memberId") Long memberId
    ) {
        return new ResponseEntity<>(this.memberService.getMemberInfo(memberId), HttpStatus.OK);
    }
}
