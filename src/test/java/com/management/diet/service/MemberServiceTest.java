package com.management.diet.service;

import com.management.diet.configuration.security.auth.MyUserDetailsService;
import com.management.diet.configuration.security.jwt.TokenProvider;
import com.management.diet.dto.request.MemberLoginDto;
import com.management.diet.dto.request.MemberRequestDto;
import com.management.diet.dto.response.MemberLoginResponseDto;
import com.management.diet.dto.response.MemberResponseDto;
import com.management.diet.enums.Theme;
import com.management.diet.exception.exception.DuplicateMemberException;
import com.management.diet.exception.exception.MemberNotFindException;
import com.management.diet.repository.MemberRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("dev")
@SpringBootTest
class MemberServiceTest {
    @Autowired
    private MemberService memberService;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private TokenProvider tokenProvider;
    @Autowired
    private MyUserDetailsService myUserDetailsService;

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

    @Test
    public void notDuplicateMember(){
        //given
        MemberRequestDto memberRequestDto = new MemberRequestDto("test@gmail.com", "test", "1234", Theme.BLACK);
        memberService.join(memberRequestDto);

        //when
        MemberRequestDto test = new MemberRequestDto("test@gmail.com", "test", "1234", Theme.BLACK);

        //then
        org.junit.jupiter.api.Assertions.assertThrows(DuplicateMemberException.class, ()->memberService.join(test));
    }

    @Test
    public void login(){
        //given
        MemberRequestDto memberRequestDto = new MemberRequestDto("test@gmail.com", "test", "1234", Theme.BLACK);
        memberService.join(memberRequestDto);
        MemberLoginDto memberLoginDto = new MemberLoginDto("test@gmail.com", "1234");

        //when
        MemberLoginResponseDto login = memberService.login(memberLoginDto);

        //then
        Assertions.assertThat(tokenProvider.getUserEmail(login.getAccessToken())).isEqualTo("test@gmail.com");
    }

    @Test
    public void logout(){
        //given
        MemberRequestDto memberRequestDto = new MemberRequestDto("test@gmail.com", "test", "1234", Theme.BLACK);
        memberService.join(memberRequestDto);
        UserDetails userDetails = myUserDetailsService.loadUserByUsername(memberRequestDto.getEmail());
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);

        //when
        memberService.logout();

        //then
        Assertions.assertThat(memberService.findMemberByEmail("test@gmail.com").getRefreshToken()).isEqualTo(null);
    }

    @Test
    public void withdrawal(){
        //given
        MemberRequestDto memberRequestDto = new MemberRequestDto("test@gmail.com", "test", "1234", Theme.BLACK);
        memberService.join(memberRequestDto);
        UserDetails userDetails = myUserDetailsService.loadUserByUsername(memberRequestDto.getEmail());
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);

        //when
        memberService.withdrawalMember();

        //then
        assertThrows(MemberNotFindException.class, () -> memberService.findMemberByEmail(memberRequestDto.getEmail()));
    }

    @Test
    public void findByMemberIdx(){
        //given
        MemberRequestDto memberRequestDto = new MemberRequestDto("test@gmail.com", "test", "1234", Theme.BLACK);
        Long memberIdx = memberService.join(memberRequestDto);

        //when
        MemberResponseDto member = memberService.findMemberByIdx(memberIdx);

        //then
        Assertions.assertThat(member.getName()).isEqualTo(memberRequestDto.getName());
    }
}