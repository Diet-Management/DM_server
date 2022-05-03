package com.management.diet.controller;

import com.management.diet.dto.request.MemberLoginDto;
import com.management.diet.dto.request.MemberRequestDto;
import com.management.diet.dto.response.MemberLoginResponseDto;
import com.management.diet.dto.response.MemberResponseDto;
import com.management.diet.response.ResponseService;
import com.management.diet.response.result.CommonResultResponse;
import com.management.diet.response.result.SingleResultResponse;
import com.management.diet.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1")
public class MemberController {
    private final MemberService memberService;
    private final ResponseService responseService;

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
        memberService.uploadProfile(file, Authorization);
        return responseService.getSuccessResult();
    }

    @GetMapping("/member/profile/{memberIdx}")
    public ResponseEntity<Resource> viewImg(@PathVariable Long memberIdx) throws IOException{
        return memberService.getProfile(memberIdx);
    }

    @GetMapping("/member/token")
    public SingleResultResponse<MemberLoginResponseDto> newToken(@RequestHeader String refreshToken){
        return responseService.getSingleResult(memberService.generateNewAccessToken(refreshToken));
    }
}
