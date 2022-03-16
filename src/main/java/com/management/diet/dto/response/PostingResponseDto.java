package com.management.diet.dto.response;

import com.management.diet.domain.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@Builder
@AllArgsConstructor @NoArgsConstructor
public class PostingResponseDto {
    private String title;
    private String content;
    private Member member;
    private LocalDate date;
}
