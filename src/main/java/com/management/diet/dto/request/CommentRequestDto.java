package com.management.diet.dto.request;

import com.management.diet.domain.Comment;
import com.management.diet.domain.Member;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Getter
@AllArgsConstructor @NoArgsConstructor
public class CommentRequestDto {

    @NotBlank
    private String content;

    public Comment toEntity(Member member){
        return Comment.builder()
                .content(content)
                .writer(member)
                .build();
    }
}
