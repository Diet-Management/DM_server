package com.management.diet.service;

import com.management.diet.configuration.security.jwt.TokenProvider;
import com.management.diet.domain.Member;
import com.management.diet.dto.request.MemberLoginDto;
import com.management.diet.dto.request.MemberRequestDto;
import com.management.diet.dto.response.MemberLoginResponseDto;
import com.management.diet.dto.response.MemberResponseDto;
import com.management.diet.exception.ErrorCode;
import com.management.diet.exception.exception.*;
import com.management.diet.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;


@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    private final TokenProvider tokenProvider;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public Long join(MemberRequestDto memberRequestDto){
        Optional<Member> byEmail = memberRepository.findByEmail(memberRequestDto.getEmail());
        if(!byEmail.isEmpty()){
            throw new DuplicateMemberException("Member is duplicate", ErrorCode.DUPLICATE_MEMBER);
        }
        memberRequestDto.setPassword(passwordEncoder.encode(memberRequestDto.getPassword()));
        Member member = memberRequestDto.toEntity();
        return memberRepository.save(member).getMember_idx();
    }

    @Transactional
    public MemberLoginResponseDto login(MemberLoginDto memberLoginDto){
        Member member = memberRepository.findByEmail(memberLoginDto.getEmail())
                .orElseThrow(() -> new MemberNotExistsException("Member can't find Exception", ErrorCode.MEMBER_NOT_FIND));
        if(!passwordEncoder.matches(memberLoginDto.getPassword(), member.getPassword())){
            throw new PasswordNotCorrectException("Password isn't correct", ErrorCode.PASSWORD_NOT_CORRECT);
        }
        final String accessToken=tokenProvider.generateAccessToken(member.getEmail());
        final String refreshToken=tokenProvider.generateRefreshToken(member.getEmail());
        member.updateRefreshToken(refreshToken);
        MemberLoginResponseDto responseDto = MemberLoginResponseDto.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
        return responseDto;
    }

    @Transactional
    public void withdrawalMember(String accessToken){
        IsAccessTokenExpired(accessToken);
        String userEmail = getUserEmail(accessToken);
        Member member = memberRepository.findByEmail(userEmail)
                .orElseThrow(() -> new MemberNotFindException("Member can't find to accessToken", ErrorCode.MEMBER_NOT_FIND));
        memberRepository.delete(member);
    }

    protected String getUserEmail(String accessToken){
        return tokenProvider.getUserEmail(accessToken);
    }
    protected void IsAccessTokenExpired(String accessToken) {
        if(tokenProvider.isTokenExpired(accessToken)){
            throw new AccessTokenExpiredException("AccessToken is expired", ErrorCode.ACCESS_TOKEN_EXPIRED);
        }
    }

    @Transactional
    public void uploadProfile(String fileName,String accessToken){
        IsAccessTokenExpired(accessToken);
        Member member = memberRepository.findByEmail(getUserEmail(accessToken))
                .orElseThrow(() -> new MemberNotFindException("Member can't find to accessToken", ErrorCode.MEMBER_NOT_FIND));
        member.updateProfile(fileName);
    }

    @Transactional(readOnly = true)
    public MemberResponseDto findMemberByIdx(Long memberIdx){
        Member member = memberRepository.findById(memberIdx)
                .orElseThrow(() -> new MemberNotFindException("Member can't find", ErrorCode.MEMBER_NOT_FIND));
        MemberResponseDto memberResponseDto = MemberResponseDto.builder()
                .memberIdx(memberIdx)
                .name(member.getName())
                .profile(member.getProfile())
                .postings(member.getPostings())
                .theme(member.getTheme())
                .build();
        return memberResponseDto;
    }

    @Transactional(readOnly = true)
    public Member findMemberByEmail(String email){
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new MemberNotFindException("Member can't find", ErrorCode.MEMBER_NOT_FIND));
        return member;
    }

    public MemberLoginResponseDto generateNewAccessToken(String refreshToken){
        if(tokenProvider.isTokenExpired(refreshToken)){
            throw new RefreshTokenExpiredException("RefreshToken is expired", ErrorCode.REFRESH_TOKEN_EXPIRED);
        }
        String userEmail = getUserEmail(refreshToken);
        String accessToken = tokenProvider.generateAccessToken(userEmail);
        return MemberLoginResponseDto.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }
}
