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
import com.management.diet.util.CurrentMemberUtil;
import com.management.diet.util.ResponseDtoUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;


@Service
@Slf4j
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    private final TokenProvider tokenProvider;
    private final PasswordEncoder passwordEncoder;
    private final CurrentMemberUtil memberUtil;
    private final ResponseDtoUtil responseDtoUtil;
    @Value("${file.upload.location}")
    private String fileDir;

    @Transactional
    public Long join(MemberRequestDto memberRequestDto){
        Optional<Member> byEmail = memberRepository.findByEmail(memberRequestDto.getEmail());
        if(!byEmail.isEmpty()){
            throw new DuplicateMemberException("Member is duplicate", ErrorCode.DUPLICATE_MEMBER);
        }
        Member member = memberRequestDto.toEntity(passwordEncoder.encode(memberRequestDto.getPassword()));
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
                .memberIdx(member.getMember_idx())
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
        return responseDto;
    }

    @Transactional
    public void logout(){
        Member currentMember = getCurrentMember();
        currentMember.updateRefreshToken(null);
    }

    @Transactional
    public void withdrawalMember(){
        Member member = getCurrentMember();
        memberRepository.delete(member);
    }

    @Transactional
    public void uploadProfile(MultipartFile file){
        log.info(" fileDir = {}",fileDir);
        if(file.isEmpty()){
            throw new FileNotExistsException("File doesn't exist", ErrorCode.FILE_NOT_EXISTS);
        }
        String fullPath = fileDir + file.getOriginalFilename();
        log.info(" fileDir = {}",fullPath);
        try{
            file.transferTo(new File(fullPath));
        }catch (IOException e){
            throw new WrongPathException("Path isn't right",ErrorCode.WRONG_PATH);
        }
        Member member = getCurrentMember();
        member.updateProfile(fullPath);
    }

    @Transactional(readOnly = true)
    public ResponseEntity<Resource> getProfile(Long memberIdx) throws IOException {
        String profile = findMemberByIdx(memberIdx).getProfile();
        if (profile==null){
            throw new ProfileNotExistsException("Profile picture doesn't exist", ErrorCode.PROFILE_NOT_EXISTS);
        }
        Path path=new File(profile).toPath();
        FileSystemResource resource = new FileSystemResource(path);
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(Files.probeContentType(path)))
                .body(resource);
    }

    @Transactional(readOnly = true)
    public MemberResponseDto findMemberByIdx(Long memberIdx){
        Member member = memberRepository.findById(memberIdx)
                .orElseThrow(() -> new MemberNotFindException("Member can't find", ErrorCode.MEMBER_NOT_FIND));
        MemberResponseDto memberResponseDto = (MemberResponseDto) responseDtoUtil.oneToResponse(new MemberResponseDto(), member);
        return memberResponseDto;
    }

    @Transactional(readOnly = true)
    public Member findMemberByEmail(String email){
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new MemberNotFindException("Member can't find", ErrorCode.MEMBER_NOT_FIND));
        return member;
    }

    public Member getCurrentMember(){
        return memberUtil.getCurrentMember();
    }

}
