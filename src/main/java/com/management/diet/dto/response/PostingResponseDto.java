package com.management.diet.dto.response;

import com.management.diet.domain.Comment;
import com.management.diet.domain.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PostingResponseDto implements ResponseDto{
    private Long postIdx;
    private String title;
    private String content;
    private LocalDate date;
    private int goods;
    private Boolean fix;
    private List<Comment> comments;
    private Member member;
}
