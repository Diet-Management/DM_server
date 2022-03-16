package com.management.diet.dto.response;

import com.management.diet.domain.Posting;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
@Getter
public class MemberResponseDto {
    private Long memberIdx;
    private String name;
    private String profile;
    private List<Posting> postings;
}
