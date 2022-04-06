package com.management.diet.service;

import com.management.diet.dto.request.MemberRequestDto;
import com.management.diet.dto.response.MemberResponseDto;
import com.management.diet.enums.Theme;
import com.management.diet.repository.MemberRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class MemberServiceTest {
    @Autowired
    private MemberService memberService;
    @Autowired
    private MemberRepository memberRepository;

    @AfterEach
    public void init(){
        memberRepository.deleteAll();
    }

    @Test
    public void join(){
        //given
        MemberRequestDto memberRequestDto = new MemberRequestDto("test@gmail.com", "test", "1234", Theme.BLACK);

        //when
        Long join = memberService.join(memberRequestDto);
        MemberResponseDto member = memberService.findMemberByIdx(join);

        //then
        Assertions.assertThat(member.getMemberIdx()).isEqualTo(join);
        Assertions.assertThat(member.getName()).isEqualTo(memberRequestDto.getName());
    }
}