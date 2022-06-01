package com.management.diet.util;

import com.management.diet.domain.Member;
import com.management.diet.domain.Posting;
import com.management.diet.dto.response.MemberLoginResponseDto;
import com.management.diet.dto.response.MemberResponseDto;
import com.management.diet.dto.response.PostingResponseDto;
import com.management.diet.dto.response.ResponseDto;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class ResponseDtoUtil {

    public <T> ResponseDto oneToResponse(ResponseDto responseDto, T data){
        return isSupport(responseDto, data);
    }

    private <T>ResponseDto isSupport(ResponseDto responseDto, T data) {
        if(responseDto instanceof PostingResponseDto){
            PostingResponseDto postingResponseDto = (PostingResponseDto) responseDto;
            if(data instanceof Posting){
                Posting posting = (Posting) data;
                postingResponseDto = PostingResponseDto.builder()
                        .postIdx(posting.getPostingIdx())
                        .title(posting.getTitle())
                        .content(posting.getContent())
                        .member(posting.getMember())
                        .comments(posting.getComments())
                        .date(posting.getDate())
                        .fix(posting.getFix())
                        .goods(posting.getGoods())
                        .build();
                return postingResponseDto;
            }
            else{
                throw new RuntimeException();
            }
        }
        else if(responseDto instanceof MemberResponseDto){
            MemberResponseDto memberResponseDto = (MemberResponseDto) responseDto;
            if(data instanceof Member){
                Member member = (Member) data;
                memberResponseDto = MemberResponseDto.builder()
                        .memberIdx(member.getMember_idx())
                        .name(member.getName())
                        .postings(member.getPostings())
                        .profile(member.getProfile())
                        .theme(member.getTheme())
                        .build();
                return memberResponseDto;
            }
            else{
                throw new RuntimeException();
            }

        }
        else if(responseDto instanceof MemberLoginResponseDto){
            responseDto = (MemberLoginResponseDto) responseDto;
        }
        else{
            throw new RuntimeException();
        }
        return null;
    }
}
