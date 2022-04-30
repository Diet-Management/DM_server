package com.management.diet.controller;

import com.management.diet.dto.request.PostingRequestDto;
import com.management.diet.dto.response.PostingResponseDto;
import com.management.diet.enums.SortBy;
import com.management.diet.response.ResponseService;
import com.management.diet.response.result.CommonResultResponse;
import com.management.diet.response.result.ListResultResponse;
import com.management.diet.response.result.SingleResultResponse;
import com.management.diet.service.PostingService;
import lombok.RequiredArgsConstructor;
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
    public ListResultResponse<PostingResponseDto> findAll(){
        return responseService.getListResult(postingService.findAll());
    }

    @GetMapping("/posting/goods/asc")
    public ListResultResponse<PostingResponseDto> findAllGoodsSortByAsc(){
        return responseService.getListResult(postingService.findAllByGoods(SortBy.ASC));
    }

    @GetMapping("/posting/goods/desc")
    public ListResultResponse<PostingResponseDto> findAllGoodsSortByDesc(){
        return responseService.getListResult(postingService.findAllByGoods(SortBy.DESC));
    }

    @GetMapping("/posting/date/desc")
    public ListResultResponse<PostingResponseDto> findAllDateSortByAsc(){
        return responseService.getListResult(postingService.findAllByDate(SortBy.ASC));
    }

    @GetMapping("/posting/date/asc")
    public ListResultResponse<PostingResponseDto> findAllDateSortByDesc(){
        return responseService.getListResult(postingService.findAllByDate(SortBy.DESC));
    }

    @GetMapping("/posting/keyWord")
    public ListResultResponse<PostingResponseDto> findAllByKeyWord(@RequestParam String keyWord){
        return responseService.getListResult(postingService.findByKeyWord(keyWord));
    }

    @DeleteMapping("/posting/{postingIdx}")
    public CommonResultResponse deletePosting(@RequestHeader String Authorization, @PathVariable Long postingIdx){
        postingService.deletePosting(Authorization, postingIdx);
        return responseService.getSuccessResult();
    }

    @PutMapping("/posting/{postingIdx}")
    public CommonResultResponse updatePosting(@RequestHeader String Authorization, @PathVariable Long postingIdx, @RequestBody PostingRequestDto postingRequestDto){
        postingService.updatePosting(Authorization, postingIdx, postingRequestDto);
        return responseService.getSuccessResult();
    }

    @PatchMapping("/posting/good/add/{postIdx}")
    public CommonResultResponse addGood(@PathVariable Long postIdx){
        postingService.addGoods(postIdx);
        return responseService.getSuccessResult();
    }

    @PatchMapping("/posting/good/minus/{postIdx}")
    public CommonResultResponse minusGood(@PathVariable Long postIdx){
        postingService.minusGoods(postIdx);
        return responseService.getSuccessResult();
    }
}
