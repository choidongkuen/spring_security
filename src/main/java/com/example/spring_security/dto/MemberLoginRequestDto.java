package com.example.spring_security.dto;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MemberLoginRequestDto {

    private String memberId;

    private String password;
}
