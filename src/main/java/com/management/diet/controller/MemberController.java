package com.management.diet.controller;

import com.management.diet.dto.request.MemberLoginDto;
import com.management.diet.dto.request.MemberRequestDto;
import com.management.diet.dto.response.MemberLoginResponseDto;
import com.management.diet.dto.response.MemberResponseDto;
import com.management.diet.exception.ErrorCode;
import com.management.diet.exception.exception.FileNotExistsException;
import com.management.diet.exception.exception.WrongPathException;
import com.management.diet.response.ResponseService;
import com.management.diet.response.result.CommonResultResponse;
import com.management.diet.response.result.SingleResultResponse;
import com.management.diet.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1")
@Slf4j
public class MemberController {
    private final MemberService memberService;
    private final ResponseService responseService;
    @Value("${file.upload.location}")
    private String fileDir;

    @PostMapping("/member/join")
    public CommonResultResponse join(@RequestBody MemberRequestDto memberDto){
        memberService.join(memberDto);
        return responseService.getSuccessResult();
    }

    @PostMapping("/member/login")
    public SingleResultResponse<MemberLoginResponseDto> login(@RequestBody MemberLoginDto memberLoginDto){
        return responseService.getSingleResult(memberService.login(memberLoginDto));
    }

    @GetMapping("/member/{memberIdx}")
    public SingleResultResponse<MemberResponseDto> findMember(@PathVariable Long memberIdx){
        return responseService.getSingleResult(memberService.findMemberByIdx(memberIdx));
    }

    @DeleteMapping("/member")
    public CommonResultResponse deleteMember(@RequestHeader String Authorization){
        memberService.withdrawalMember(Authorization);
        return responseService.getSuccessResult();
    }

    @PatchMapping("/member/profile")
    public CommonResultResponse updateProfile(@RequestParam MultipartFile file, @RequestHeader String Authorization){
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
        memberService.uploadProfile(fullPath, Authorization);
        return responseService.getSuccessResult();
    }

    @GetMapping("/member/profile/{memberIdx}")
    public ResponseEntity<Resource> viewImg(@PathVariable Long memberIdx) throws IOException{
        String profile = memberService.findMemberByIdx(memberIdx).getProfile();
        if (profile==null){
            throw new RuntimeException();
        }
        Path path=new File(profile).toPath();
        FileSystemResource resource = new FileSystemResource(path);
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(Files.probeContentType(path)))
                .body(resource);
    }
}
