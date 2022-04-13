package com.management.diet.service;

import com.management.diet.dto.request.CommentRequestDto;
import com.management.diet.dto.request.MemberLoginDto;
import com.management.diet.dto.request.MemberRequestDto;
import com.management.diet.dto.request.PostingRequestDto;
import com.management.diet.dto.response.MemberLoginResponseDto;
import com.management.diet.dto.response.PostingResponseDto;
import com.management.diet.enums.Theme;
import com.management.diet.repository.CommentRepository;
import com.management.diet.repository.MemberRepository;
import com.management.diet.repository.PostingRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("dev")
@SpringBootTest
class CommentServiceTest {
    @Autowired
    private PostingService postingService;
    @Autowired
    private MemberService memberService;
    @Autowired
    private CommentService commentService;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private PostingRepository postingRepository;
    @Autowired
    private CommentRepository commentRepository;

    private MemberLoginResponseDto login;

    @BeforeEach
    public void initialSetting(){
        MemberRequestDto memberRequestDto = new MemberRequestDto("test@gmail.com", "test", "1234", Theme.BLACK);
        memberService.join(memberRequestDto);
        MemberLoginDto memberLoginDto = new MemberLoginDto("test@gmail.com", "1234");
        login = memberService.login(memberLoginDto);

        PostingRequestDto postingRequestDto = new PostingRequestDto("title", "content");
        postingService.save(postingRequestDto, login.getAccessToken());
    }

    @AfterEach
    public void init(){
        memberRepository.deleteAll();
        postingRepository.deleteAll();
    }

    @Test
    public void save(){
        //given
        CommentRequestDto commentRequestDto = new CommentRequestDto("test");

        //when
        commentService.writeComment(commentRequestDto,2L, login.getAccessToken());

        //then
        PostingResponseDto posting = postingService.getByIdx(2L);
        Assertions.assertThat(posting.getComments().get(0).getContent()).isEqualTo(commentRequestDto.getContent());
        Assertions.assertThat(posting.getComments().size()).isEqualTo(1);
    }
}