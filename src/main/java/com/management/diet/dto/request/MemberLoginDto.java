package com.management.diet.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.validation.constraints.Email;

@Getter
@AllArgsConstructor
public class MemberLoginDto {
    @Email
    private String email;
    private String password;
}
