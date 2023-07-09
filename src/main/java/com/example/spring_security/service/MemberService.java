package com.example.spring_security.service;

import com.example.spring_security.domain.entity.Member;
import com.example.spring_security.domain.jwt.JwtProvider;
import com.example.spring_security.domain.jwt.TokenInfo;
import com.example.spring_security.domain.repository.MemberRepository;
import com.example.spring_security.dto.GetMemberInfoResponseDto;
import com.example.spring_security.dto.MemberLoginRequestDto;
import com.example.spring_security.dto.MemberSignUpRequestDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Service
public class MemberService {
    private final MemberRepository memberRepository;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;

    @Transactional
    public Long signUp(MemberSignUpRequestDto request) {

        checkSignupPossibility(request);

        Member member = this.memberRepository.save(request.toEntity());
        member.encodePassword(passwordEncoder.encode(member.getPassword()));
        return member.getId();

    }

    @Transactional
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

    private void checkSignupPossibility(MemberSignUpRequestDto request) {
        if (this.memberRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new RuntimeException("이미 존재하는 회원입니다.");
        }
        if (!request.getPassword().equals(request.getRePassword())) {
            throw new RuntimeException("비밀번호가 일치하지 않습니다.");
        }
    }

    @Transactional(readOnly = true)
    public GetMemberInfoResponseDto getMemberInfo(Long memberId) {
        Member member = this.memberRepository.findById(memberId)
                .orElseThrow(() -> new RuntimeException("일치하는 회원이 존재하지 않습니다."));

        return GetMemberInfoResponseDto.from(member);
    }
}
