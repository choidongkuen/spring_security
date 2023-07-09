package com.example.spring_security.dto;

import lombok.*;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MemberLoginRequestDto {

    @NotBlank
    private String memberId;

    @NotBlank
    private String password;
}
