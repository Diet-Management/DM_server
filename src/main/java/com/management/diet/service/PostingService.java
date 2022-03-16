package com.management.diet.service;

import com.management.diet.domain.Member;
import com.management.diet.domain.Posting;
import com.management.diet.dto.request.PostingRequestDto;
import com.management.diet.dto.response.MemberResponseDto;
import com.management.diet.dto.response.PostingResponseDto;
import com.management.diet.repository.PostingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PostingService {
    private final PostingRepository postingRepository;
    private final MemberService memberService;

    @Transactional
    public Long save(PostingRequestDto postingRequestDto,String accessToken){
        String userEmail = memberService.getUserEmail(accessToken);
        Member member = memberService.findMemberByEmail(userEmail);
        Posting posting = postingRequestDto.toEntity(member, LocalDate.now());
        member.getPostings().add(posting);
        postingRepository.save(posting);
        return posting.getPostingIdx();
    }

    @Transactional
    public void deletePosting(String accessToken,Long postingIdx){
        String userEmail = memberService.getUserEmail(accessToken);
        Member member = memberService.findMemberByEmail(userEmail);
        Posting posting = getPostingByIdx(postingIdx);
        Member writer = posting.getMember();
        if(member != writer){
            throw new RuntimeException();
        }
        postingRepository.delete(posting);
    }

    @Transactional
    public void updatePosting(String accessToken,Long postingIdx, PostingRequestDto postingRequestDto){
        String userEmail = memberService.getUserEmail(accessToken);
        Member member = memberService.findMemberByEmail(userEmail);
        Posting posting = getPostingByIdx(postingIdx);
        Member writer = posting.getMember();
        if(member != writer){
            throw new RuntimeException();
        }
        posting.update(postingRequestDto);
    }

    @Transactional(readOnly = true)
    protected Posting getPostingByIdx(Long postingIdx){
        return postingRepository.findById(postingIdx)
                .orElseThrow(()->new RuntimeException());
    }

    @Transactional(readOnly = true)
    public PostingResponseDto getByIdx(Long postingIdx){
        Posting posting = postingRepository.findById(postingIdx)
                .orElseThrow(() -> new RuntimeException());
        return PostingResponseDto.builder()
                .postIdx(posting.getPostingIdx())
                .title(posting.getTitle())
                .content(posting.getContent())
                .fix(posting.getFix())
                .member(posting.getMember())
                .date(posting.getDate())
                .build();
    }

    @Transactional(readOnly = true)
    public List<PostingResponseDto> findAll(){
        List<Posting> all = postingRepository.findAll();
        List<PostingResponseDto> response= new ArrayList<>();
        all.forEach(i->response.add(PostingResponseDto.builder()
                        .postIdx(i.getPostingIdx())
                        .title(i.getTitle())
                        .date(i.getDate())
                        .member(i.getMember())
                        .content(i.getContent())
                        .fix(i.getFix())
                .build()));
        return response;
    }
}
