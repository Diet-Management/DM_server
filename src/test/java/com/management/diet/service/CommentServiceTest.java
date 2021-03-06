package com.management.diet.service;

import com.management.diet.configuration.security.auth.MyUserDetailsService;
import com.management.diet.domain.Comment;
import com.management.diet.dto.request.CommentRequestDto;
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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

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
    private MyUserDetailsService myUserDetailsService;

    private Long postingIdx;

    @BeforeEach
    public void initialSetting(){
        MemberRequestDto memberRequestDto = new MemberRequestDto("test@gmail.com", "test", "1234", Theme.BLACK);
        memberService.join(memberRequestDto);
        UserDetails userDetails = myUserDetailsService.loadUserByUsername(memberRequestDto.getEmail());
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);

        PostingRequestDto postingRequestDto = new PostingRequestDto("title", "content");
        postingIdx = postingService.save(postingRequestDto);
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
        commentService.writeComment(commentRequestDto,postingIdx);

        //then
        PostingResponseDto posting = postingService.getByIdx(postingIdx);
        Assertions.assertThat(posting.getComments().get(0).getContent()).isEqualTo(commentRequestDto.getContent());
        Assertions.assertThat(posting.getComments().size()).isEqualTo(1);
    }

    @Test
    public void delete(){
        //given
        CommentRequestDto commentRequestDto = new CommentRequestDto("test");
        Long commentIdx = commentService.writeComment(commentRequestDto, postingIdx);

        //when
        commentService.delete(commentIdx);

        //then
        List<Comment> comments = postingService.getPostingByIdx(postingIdx).getComments();
        assertThrows(IndexOutOfBoundsException.class, () -> comments.get(0));
        Assertions.assertThat(comments.size()).isEqualTo(0);
    }

    @Test
    public void update(){
        //given
        CommentRequestDto commentRequestDto = new CommentRequestDto("test");
        Long commentIdx = commentService.writeComment(commentRequestDto, postingIdx);
        Assertions.assertThat(postingService.getPostingByIdx(postingIdx).getComments().get(0).getContent()).isEqualTo(commentRequestDto.getContent());

        //when
        CommentRequestDto commentRequestDto2 = new CommentRequestDto("content");
        commentService.update(commentRequestDto2, commentIdx);

        //then
        Assertions.assertThat(postingService.getPostingByIdx(postingIdx).getComments().get(0).getContent()).isEqualTo(commentRequestDto2.getContent());
    }
}