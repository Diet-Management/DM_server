package com.management.diet.service;

import com.management.diet.configuration.security.auth.MyUserDetailsService;
import com.management.diet.dto.request.MemberRequestDto;
import com.management.diet.dto.request.PostingRequestDto;
import com.management.diet.dto.response.MemberLoginResponseDto;
import com.management.diet.dto.response.PostingResponseDto;
import com.management.diet.enums.Theme;
import com.management.diet.exception.exception.PostingNotFindException;
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
class PostingServiceTest {
    @Autowired
    private PostingService postingService;
    @Autowired
    private MemberService memberService;
    @Autowired
    private PostingRepository postingRepository;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private MyUserDetailsService myUserDetailsService;

    @BeforeEach
    public void createMember(){
        MemberRequestDto memberRequestDto = new MemberRequestDto("test@gmail.com", "test", "1234", Theme.BLACK);
        memberService.join(memberRequestDto);
        UserDetails userDetails = myUserDetailsService.loadUserByUsername(memberRequestDto.getEmail());
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
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
        Long save = postingService.save(postingRequestDto);

        //then
        Assertions.assertThat(postingRequestDto.getTitle()).isEqualTo(postingService.getPostingByIdx(save).getTitle());
    }

    @Test
    public void findOne(){
        //given
        PostingRequestDto postingRequestDto = new PostingRequestDto("title", "content");
        Long save = postingService.save(postingRequestDto);

        //when
        PostingResponseDto posting = postingService.getByIdx(save);

        //then
        Assertions.assertThat(posting.getTitle()).isEqualTo(postingRequestDto.getTitle());
        Assertions.assertThat(posting.getContent()).isEqualTo(postingRequestDto.getContent());
    }

    @Test
    public void deletePosting(){
        //given
        PostingRequestDto postingRequestDto = new PostingRequestDto("title", "content");
        Long save = postingService.save(postingRequestDto);

        //when
        postingService.deletePosting(save);

        //then
        assertThrows(PostingNotFindException.class, ()-> postingService.getByIdx(save));
    }

    @Test
    public void findAll(){
        //given
        PostingRequestDto postingRequestDto = new PostingRequestDto("title", "content");
        PostingRequestDto postingRequestDto2 = new PostingRequestDto("test", "몰?루");
        postingService.save(postingRequestDto);
        postingService.save(postingRequestDto2);

        //when
        List<PostingResponseDto> all = postingService.findAll();

        //then
        Assertions.assertThat(all.size()).isEqualTo(2);
        Assertions.assertThat(all.get(0).getTitle()).isEqualTo("title");
        Assertions.assertThat(all.get(0).getContent()).isEqualTo("content");
        Assertions.assertThat(all.get(1).getTitle()).isEqualTo("test");
        Assertions.assertThat(all.get(1).getContent()).isEqualTo("몰?루");
    }
}