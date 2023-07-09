package com.example.spring_security.dto;

import com.example.spring_security.domain.entity.Member;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GetMemberInfoResponseDto {
    private String name;

    private String email;

    public static GetMemberInfoResponseDto from(Member member) {
        return GetMemberInfoResponseDto.builder()
                .name(member.getName())
                .email(member.getEmail())
                .build();
    }
}
