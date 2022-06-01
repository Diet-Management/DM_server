package com.management.diet.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MemberLoginResponseDto implements ResponseDto{
    private Long memberIdx;
    private String accessToken;
    private String refreshToken;
}
