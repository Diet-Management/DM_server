package com.management.diet.dto.request;

import com.management.diet.domain.Member;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Setter @Getter
@AllArgsConstructor
public class MemberRequestDto {
    @NotBlank(message = "email is not allowed null") @Email
    private String email;

    @NotBlank(message = "name is not allowed null")
    private String name;

    @NotBlank(message = "password is not allowed null")
    private String password;

    public Member toEntity(){
        return Member.builder()
                .email(email)
                .name(name)
                .password(password)
                .build();
    }
}
