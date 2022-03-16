package com.management.diet.controller;

import com.management.diet.domain.Posting;
import com.management.diet.dto.request.PostingRequestDto;
import com.management.diet.dto.response.PostingResponseDto;
import com.management.diet.response.ResponseService;
import com.management.diet.response.result.CommonResultResponse;
import com.management.diet.response.result.ListResultResponse;
import com.management.diet.response.result.SingleResultResponse;
import com.management.diet.service.PostingService;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1")
public class PostingController {
    private final PostingService postingService;
    private final ResponseService responseService;

    @PostMapping("/posting")
    public CommonResultResponse savePosting(@RequestBody PostingRequestDto postingRequestDto, @RequestHeader String Authorization){
        postingService.save(postingRequestDto, Authorization);
        return responseService.getSuccessResult();
    }

    @GetMapping("/posting/{postingIdx}")
    public SingleResultResponse<PostingResponseDto> findByIdx(@PathVariable Long postingIdx){
        return responseService.getSingleResult(postingService.getByIdx(postingIdx));
    }

    @GetMapping("/posting")
    public ListResultResponse<Posting> findAll(){
        return responseService.getListResult(postingService.findAll());
    }
}
