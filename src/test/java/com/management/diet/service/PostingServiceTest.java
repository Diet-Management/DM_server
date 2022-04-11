package com.management.diet.service;

import com.management.diet.dto.request.MemberLoginDto;
import com.management.diet.dto.request.MemberRequestDto;
import com.management.diet.dto.request.PostingRequestDto;
import com.management.diet.dto.response.MemberLoginResponseDto;
import com.management.diet.dto.response.PostingResponseDto;
import com.management.diet.enums.Theme;
import com.management.diet.repository.MemberRepository;
import com.management.diet.repository.PostingRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.event.annotation.BeforeTestExecution;
import org.springframework.test.context.event.annotation.BeforeTestMethod;

import javax.annotation.PostConstruct;

import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("dev")
@SpringBootTest
class PostingServiceTest {
    private MemberLoginResponseDto login;

    @Autowired
    private PostingService postingService;
    @Autowired
    private MemberService memberService;
    @Autowired
    private PostingRepository postingRepository;
    @Autowired
    private MemberRepository memberRepository;

    @BeforeEach
    public void createMember(){
        MemberRequestDto memberRequestDto = new MemberRequestDto("test@gmail.com", "test", "1234", Theme.BLACK);
        memberService.join(memberRequestDto);
        MemberLoginDto memberLoginDto = new MemberLoginDto("test@gmail.com", "1234");
        login = memberService.login(memberLoginDto);
    }
    @AfterEach
    public void init(){
        memberRepository.deleteAll();
        postingRepository.deleteAll();
    }

    @Test
    public void save(){
        //given
        PostingRequestDto postingRequestDto = new PostingRequestDto("title", "content");

        //when
        Long save = postingService.save(postingRequestDto, login.getAccessToken());

        //then
        Assertions.assertThat(postingRequestDto.getTitle()).isEqualTo(postingService.getPostingByIdx(save).getTitle());
    }

    @Test
    public void findOne(){
        PostingRequestDto postingRequestDto = new PostingRequestDto("title", "content");
        Long save = postingService.save(postingRequestDto, login.getAccessToken());

        PostingResponseDto posting = postingService.getByIdx(save);

        Assertions.assertThat(posting.getTitle()).isEqualTo(postingRequestDto.getTitle());
        Assertions.assertThat(posting.getContent()).isEqualTo(postingRequestDto.getContent());
    }
}