package com.management.diet.service;

import com.management.diet.configuration.security.jwt.TokenProvider;
import com.management.diet.domain.Member;
import com.management.diet.dto.request.MemberLoginDto;
import com.management.diet.dto.request.MemberRequestDto;
import com.management.diet.dto.response.MemberLoginResponseDto;
import com.management.diet.dto.response.MemberResponseDto;
import com.management.diet.exception.ErrorCode;
import com.management.diet.exception.exception.MemberNotExistsException;
import com.management.diet.exception.exception.MemberNotFindException;
import com.management.diet.exception.exception.PasswordNotCorrectException;
import com.management.diet.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    private final TokenProvider tokenProvider;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public Long join(MemberRequestDto memberRequestDto){
        memberRequestDto.setPassword(passwordEncoder.encode(memberRequestDto.getPassword()));
        Member member = memberRequestDto.toEntity();
        memberRepository.save(member);
        return member.getMember_idx();
    }

    @Transactional
    public MemberLoginResponseDto login(MemberLoginDto memberLoginDto){
        Member member = memberRepository.findByEmail(memberLoginDto.getEmail())
                .orElseThrow(() -> new MemberNotExistsException("Member can't find Exception", ErrorCode.MEMBER_NOT_FIND));
        if(!passwordEncoder.matches(memberLoginDto.getPassword(), member.getPassword())){
            throw new PasswordNotCorrectException("Password isn't correct", ErrorCode.PASSWORD_NOT_CORRECT);
        }
        final String accessToken=tokenProvider.generateAccessToken(member.getEmail());
        MemberLoginResponseDto responseDto = MemberLoginResponseDto.builder()
                .email(memberLoginDto.getEmail())
                .accessToken(accessToken)
                .build();
        return responseDto;
    }

    @Transactional
    public void withdrawalMember(String accessToken){
        String userEmail = getUserEmail(accessToken);
        Member member = memberRepository.findByEmail(userEmail)
                .orElseThrow(() -> new MemberNotFindException("Member can't find to accessToken", ErrorCode.MEMBER_NOT_FIND));
        memberRepository.delete(member);
    }

    private String getUserEmail(String accessToken){
        return tokenProvider.getUserEmail(accessToken);
    }

    @Transactional(readOnly = true)
    public MemberResponseDto findMemberByIdx(Long memberIdx){
        Member member = memberRepository.findById(memberIdx)
                .orElseThrow(() -> new MemberNotFindException("Member can't find", ErrorCode.MEMBER_NOT_FIND));
        MemberResponseDto memberResponseDto = MemberResponseDto.builder()
                .name(member.getName())
                .build();
        return memberResponseDto;
    }
}
