package com.management.diet.service;

import com.management.diet.domain.Member;
import com.management.diet.domain.Posting;
import com.management.diet.dto.request.PostingRequestDto;
import com.management.diet.enums.SortBy;
import com.management.diet.dto.response.PostingResponseDto;
import com.management.diet.exception.ErrorCode;
import com.management.diet.exception.exception.PostingNotFindException;
import com.management.diet.exception.exception.WriterNotSameException;
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
    public Long save(PostingRequestDto postingRequestDto, String accessToken){
        isTokenExpired(accessToken);
        String userEmail = memberService.getUserEmail(accessToken);
        Member member = memberService.findMemberByEmail(userEmail);
        Posting posting = postingRequestDto.toEntity(member, LocalDate.now());
        member.getPostings().add(posting);
        postingRepository.save(posting);
        return posting.getPostingIdx();
    }

    @Transactional
    public void deletePosting(String accessToken, Long postingIdx){
        isTokenExpired(accessToken);
        String userEmail = memberService.getUserEmail(accessToken);
        Member member = memberService.findMemberByEmail(userEmail);
        Posting posting = getPostingByIdx(postingIdx);
        Member writer = posting.getMember();
        if(member != writer){
            throw new WriterNotSameException("Writer isn't same", ErrorCode.WRITER_NOT_SAME);
        }
        postingRepository.delete(posting);
    }

    @Transactional
    public void updatePosting(String accessToken, Long postingIdx, PostingRequestDto postingRequestDto){
        isTokenExpired(accessToken);
        String userEmail = memberService.getUserEmail(accessToken);
        Member member = memberService.findMemberByEmail(userEmail);
        Posting posting = getPostingByIdx(postingIdx);
        Member writer = posting.getMember();
        if(member != writer){
            throw new WriterNotSameException("Writer isn't same", ErrorCode.WRITER_NOT_SAME);
        }
        posting.update(postingRequestDto);
    }

    @Transactional(readOnly = true)
    protected Posting getPostingByIdx(Long postingIdx){
        return postingRepository.findById(postingIdx)
                .orElseThrow(()-> new PostingNotFindException("Posting can't find", ErrorCode.POSTING_NOT_FIND));
    }

    @Transactional(readOnly = true)
    public PostingResponseDto getByIdx(Long postingIdx){
        Posting posting = postingRepository.findById(postingIdx)
                .orElseThrow(() -> new PostingNotFindException("Posting can't find", ErrorCode.POSTING_NOT_FIND));
        return PostingResponseDto.builder()
                .postIdx(posting.getPostingIdx())
                .title(posting.getTitle())
                .content(posting.getContent())
                .fix(posting.getFix())
                .member(posting.getMember())
                .date(posting.getDate())
                .goods(posting.getGoods())
                .comments(posting.getComments())
                .build();
    }

    @Transactional(readOnly = true)
    public List<PostingResponseDto> findAllByGoods(SortBy sortByRequestDto){
        List<Posting> all;
        if(sortByRequestDto == SortBy.ASC){
            all = postingRepository.findAllOrderByGoodsAsc();
        }
        else{
            all = postingRepository.findAllOrderByGoodsDesc();
        }
        List<PostingResponseDto> response = getPostingResponseDtoList(all);
        return response;
    }

    @Transactional(readOnly = true)
    public List<PostingResponseDto> findAllByDate(SortBy sortByDto){
        List<Posting> all;
        if(sortByDto == SortBy.ASC){
            all = postingRepository.findAllOrderByDateAsc();
        }
        else{
            all = postingRepository.findAllOrderByDateDesc();
        }
        List<PostingResponseDto> response = getPostingResponseDtoList(all);
        return response;
    }

    @Transactional(readOnly = true)
    public List<PostingResponseDto> findAll(){
        List<Posting> all = postingRepository.findAll();
        List<PostingResponseDto> response = getPostingResponseDtoList(all);
        return response;
    }

    @Transactional(readOnly = true)
    public List<PostingResponseDto> findByKeyWord(String keyWord){
        List<Posting> all = postingRepository.findAllByTitleLike("%"+keyWord+"%");
        List<PostingResponseDto> response = getPostingResponseDtoList(all);
        return response;
    }

    @Transactional
    public void addGoods(Long postingIdx){
        Posting postingByIdx = getPostingByIdx(postingIdx);
        postingByIdx.updateGoods(postingByIdx.getGoods()+1);
    }

    @Transactional
    public void minusGoods(Long postingIdx){
        Posting postingByIdx = getPostingByIdx(postingIdx);
        postingByIdx.updateGoods(postingByIdx.getGoods()-1);
    }

    private void isTokenExpired(String accessToken) {
        memberService.IsAccessTokenExpired(accessToken);
    }

    private List<PostingResponseDto> getPostingResponseDtoList(List<Posting> all) {
        List<PostingResponseDto> response= new ArrayList<>();
        all.forEach(i->response.add(PostingResponseDto.builder()
                .postIdx(i.getPostingIdx())
                .title(i.getTitle())
                .date(i.getDate())
                .member(i.getMember())
                .content(i.getContent())
                .goods(i.getGoods())
                .fix(i.getFix())
                .build()));
        return response;
    }
}
