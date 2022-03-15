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
    @NotBlank @Email
    private String email;
    @NotBlank
    private String name;
    @NotBlank
    private String password;

    public Member toEntity(){
        return Member.builder()
                .email(email)
                .name(name)
                .password(password)
                .build();
    }
}
