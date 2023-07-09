package com.example.spring_security.service;

import com.example.spring_security.domain.entity.Member;
import com.example.spring_security.domain.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final MemberRepository memberRepository;

    private final PasswordEncoder passwordEncoder;

    /*
      1. Member(실제 DB 매핑된 엔티티) 와 UserDetails 를 상속한 User 는 다른 개념
      2. MemberRepository 에서 아이디를 통해 Member 객체를 가져와서 UserDetails 로 가공하는 과정 필요!
            -> UserDetailsService(interface) 를 구현한 UserDetailsServiceImpl 가 처리
      3. UserDetails -> UserName,Password,Authorities 등이 존재 즉, 회원의 아이디,비밀번호,권한 정보를 가진다.
     */
    @Override
    public UserDetails loadUserByUsername(String userEmail) throws UsernameNotFoundException {

        return this.memberRepository.findByEmail(userEmail)
                .map(this::createUserDetails)
                .orElseThrow(() -> new UsernameNotFoundException("해당하는 유저를 찾을 수 없습니다."));

    }

    private UserDetails createUserDetails(Member member) {
        return User.builder()
                .username(member.getEmail())
                .password(member.getPassword())
                .authorities(member.getRoles()
                        .stream()
                        .map(e -> new SimpleGrantedAuthority(e.name()))
                        .collect(Collectors.toList()))
                .build();

    }
}
