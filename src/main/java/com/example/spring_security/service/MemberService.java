package com.example.spring_security.service;

import com.example.spring_security.domain.jwt.JwtProvider;
import com.example.spring_security.domain.jwt.TokenInfo;
import com.example.spring_security.domain.repository.MemberRepository;
import com.example.spring_security.dto.MemberLoginRequestDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class MemberService {
    private final MemberRepository memberRepository;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final JwtProvider jwtProvider;

    public TokenInfo login(MemberLoginRequestDto request) {
        // 1. 로그인 ID/PW 가 요청으로 들어오면 해당 정보를 기반으로 UsernamePasswordAuthenticationToken 생성
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken
                = new UsernamePasswordAuthenticationToken(request.getMemberId(), request.getPassword());

        // 2. 해당 정보를 통해 userDetailsService loadUserByUsername() 호출
        Authentication authentication
                = this.authenticationManagerBuilder.getObject().authenticate(usernamePasswordAuthenticationToken);

        // 3. 인증 완료 후 JWT 생성 by JwtProvider.createToken(Authentication authentication)
        return jwtProvider.createToken(authentication);
    }
}
