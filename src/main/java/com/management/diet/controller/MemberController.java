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
import org.springframework.web.bind.annotation.*;

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
}
