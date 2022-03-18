package com.management.diet.controller;


import com.management.diet.dto.request.CommentRequestDto;
import com.management.diet.response.ResponseService;
import com.management.diet.response.result.CommonResultResponse;
import com.management.diet.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1")
public class CommentController {
    private final CommentService commentService;
    private final ResponseService responseService;

    @PostMapping("/comment/{postingIdx}")
    public CommonResultResponse commentSave(@RequestBody CommentRequestDto commentRequestDto, @RequestHeader String Authorization, @PathVariable Long postingIdx){
        commentService.writeComment(commentRequestDto, postingIdx, Authorization);
        return responseService.getSuccessResult();
    }

    @PutMapping("/comment/{commentIdx}")
    public CommonResultResponse commentUpdate(@RequestBody CommentRequestDto commentRequestDto, @RequestHeader String Authorization, @PathVariable Long commentIdx){
        commentService.update(commentRequestDto, commentIdx, Authorization);
        return responseService.getSuccessResult();
    }

    @DeleteMapping("/comment/{commentIdx}")
    public CommonResultResponse commentDelete(@RequestHeader String Authorization, @PathVariable Long commentIdx){
        commentService.delete(Authorization, commentIdx);
        return responseService.getSuccessResult();
    }
}
