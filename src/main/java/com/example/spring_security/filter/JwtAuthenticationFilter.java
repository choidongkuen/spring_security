package com.example.spring_security.filter;

import com.example.spring_security.domain.jwt.JwtProvider;
import io.jsonwebtoken.JwtException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final List<String> EXCLUDE_URL =
            List.of("/members/login",
                    "/members/join"
            );
    private final JwtProvider jwtProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        String token = resolveToken(request);

        if (token == null) {
            log.error("JWT doesn't exist");
            throw new JwtException("JWT is null!!");
        }

        if (this.jwtProvider.validateToken(token)) {
            // 토큰이 유효할 경우 토큰에서 Authentication 객체를 가지고 와서 SecurityContext 에 저장
            Authentication authentication = jwtProvider.getAuthentication(token);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
        chain.doFilter(request, response);
    }

    private String resolveToken(HttpServletRequest request) {

        String bearerToken = request.getHeader("Authorization");

        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer")) {
            return bearerToken.substring(7);
        }
        return null;
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws SecurityException {
        return EXCLUDE_URL.stream().anyMatch(exclude -> exclude.equalsIgnoreCase(request.getServletPath()));
    }
}
