package com.management.diet.dto.request;

import com.management.diet.domain.Member;
import com.management.diet.enums.Theme;
import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Getter
@AllArgsConstructor
public class MemberRequestDto {
    @NotBlank(message = "email is not allowed null") @Email
    private String email;

    @NotBlank(message = "name is not allowed null")
    private String name;

    @NotBlank(message = "password is not allowed null")
    private String password;

    private Theme theme;

    public Member toEntity(String password){
        return Member.builder()
                .email(email)
                .name(name)
                .password(password)
                .theme(theme)
                .build();
    }
}
