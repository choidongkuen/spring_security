package com.example.spring_security.controller;

import com.example.spring_security.domain.jwt.TokenInfo;
import com.example.spring_security.dto.MemberLoginRequestDto;
import com.example.spring_security.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
public class MemberController {

    private final MemberService memberService;

    @Value("${spring.datasource.password}")
    private Long password;

    @GetMapping("/members/login")
    public ResponseEntity<TokenInfo> login(
            @RequestBody MemberLoginRequestDto request
    ) {
        return new ResponseEntity<>(this.memberService.login(request), HttpStatus.OK);
    }
}
