package com.management.diet.dto.request;

import com.management.diet.domain.Member;
import com.management.diet.domain.Posting;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@AllArgsConstructor
public class PostingRequestDto {
    private String title;
    private String content;
    public Posting toEntity(Member member, LocalDate date){
        return Posting.builder()
                .title(title)
                .content(content)
                .date(date)
                .member(member)
                .fix(false)
                .build();
    }
}
