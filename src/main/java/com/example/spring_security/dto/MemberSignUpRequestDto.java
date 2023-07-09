package com.example.spring_security.dto;

import com.example.spring_security.domain.entity.Member;
import com.example.spring_security.domain.entity.Role;
import lombok.*;

import javax.validation.constraints.NotBlank;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MemberSignUpRequestDto {

    @NotBlank
    private String email;

    @NotBlank
    private String name;

    @NotBlank
    private String password;

    @NotBlank
    private String rePassword;

    private List<Role> role;

    public Member toEntity() {
        return Member.builder()
                .email(email)
                .name(name)
                .password(password)
                .roles(role)
                .build();
    }
}
