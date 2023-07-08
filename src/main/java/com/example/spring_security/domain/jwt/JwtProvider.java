package com.example.spring_security.domain.jwt;

import antlr.Token;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import javax.naming.AuthenticationException;
import java.security.Key;
import java.util.Date;
import java.util.stream.Collectors;

@Slf4j
@Component
public class JwtProvider {

    private final Key key;

    @Value("${jwt.access.expire-time}")
    private Long accessTokenExpireTime;

    @Value("${jwt.refresh.refresh-time}")
    private Long refreshTokenExpireTime;


    /**
     * @Value 는 생성자 호출 이후에 값 주입된다
     * -> 생성자 파라미터에 적용하면 생성자 호출 시 값 주입
     */
    public JwtProvider(@Value("${JWT_SECRET_KEY}") String secretKey) {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }

    /*
        AccessToken + RefreshToken 생성하는 매소드
     */
    public TokenInfo createToken(Authentication authentication) {

        String authorities = authentication.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        long now = new Date().getTime();
        Date accessTokenExpiresIn = new Date(now + accessTokenExpireTime);

        Date refreshTokenExpiresIn = new Date(now + refreshTokenExpireTime);

        String accessToken = Jwts.builder()
                .setSubject(authentication.getName())
                .claim("authorization",authorities)
                .setExpiration(accessTokenExpiresIn)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();

        String refreshToken = Jwts.builder()
                .setExpiration(refreshTokenExpiresIn)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();

        return TokenInfo.builder()
                .grantType("Bearer")
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }


}
