package com.management.diet.dto.response;

import com.management.diet.domain.Posting;
import com.management.diet.enums.Theme;
import lombok.*;

import java.util.List;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED) @AllArgsConstructor
public class MemberResponseDto {
    private Long member_idx;
    private String name;
    private String profile;
    private Theme theme;
    private List<Posting> postings;
}
