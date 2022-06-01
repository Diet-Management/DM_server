package com.management.diet.dto.response;

import com.management.diet.domain.Posting;
import com.management.diet.enums.Theme;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@Getter
@NoArgsConstructor @AllArgsConstructor
public class MemberResponseDto implements ResponseDto{
    private Long memberIdx;
    private String name;
    private String profile;
    private Theme theme;
    private List<Posting> postings;
}
