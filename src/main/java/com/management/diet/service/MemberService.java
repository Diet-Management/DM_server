package com.management.diet.service;

import com.management.diet.configuration.security.jwt.TokenProvider;
import com.management.diet.domain.Member;
import com.management.diet.dto.request.MemberLoginDto;
import com.management.diet.dto.request.MemberRequestDto;
import com.management.diet.dto.response.MemberLoginResponseDto;
import com.management.diet.dto.response.MemberResponseDto;
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
                .orElseThrow(() -> new RuntimeException());
        if(member.getPassword()!=memberLoginDto.getEmail()){
            throw new RuntimeException();
        }
        final String accessToken=tokenProvider.generateAccessToken(member.getEmail());
        MemberLoginResponseDto responseDto = MemberLoginResponseDto.builder()
                .email(memberLoginDto.getEmail())
                .accessToken(accessToken)
                .build();
        return responseDto;
    }

    @Transactional(readOnly = true)
    public MemberResponseDto findMemberByIdx(Long memberIdx){
        Member member = memberRepository.findById(memberIdx)
                .orElseThrow(() -> new RuntimeException());
        MemberResponseDto memberResponseDto = MemberResponseDto.builder()
                .name(member.getName())
                .build();
        return memberResponseDto;
    }
}
