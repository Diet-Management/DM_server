package com.management.diet.dto.response;

import com.management.diet.domain.Comment;
import com.management.diet.domain.Member;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED) @AllArgsConstructor
public class PostingResponseDto {
    private Long postingIdx;
    private String title;
    private String content;
    private LocalDate date;
    private int goods;
    private Boolean fix;
    private List<Comment> comments;
    private Member member;
}
