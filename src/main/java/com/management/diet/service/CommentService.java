package com.management.diet.service;

import com.management.diet.domain.Comment;
import com.management.diet.domain.Member;
import com.management.diet.domain.Posting;
import com.management.diet.dto.request.CommentRequestDto;
import com.management.diet.exception.ErrorCode;
import com.management.diet.exception.exception.WriterNotSameException;
import com.management.diet.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;
    private final MemberService memberService;
    private final PostingService postingService;

    @Transactional
    public Long writeComment(CommentRequestDto commentRequestDto, Long postingIdx, String accessToken){
        memberService.IsAccessTokenExpired(accessToken);
        Member member = extracted(accessToken);
        Posting posting = postingService.getPostingByIdx(postingIdx);
        Comment comment = commentRequestDto.toEntity(member,posting);
        posting.getComments().add(comment);
        return commentRepository.save(comment).getCommentIdx();
    }

    @Transactional
    public void update(CommentRequestDto commentRequestDto, Long commentIdx, String accessToken){
        memberService.IsAccessTokenExpired(accessToken);
        Member member = extracted(accessToken);
        Comment comment = commentRepository.findById(commentIdx)
                .orElseThrow(() -> new RuntimeException());
        if(comment.getWriter() != member){
            throw new WriterNotSameException("Writer isn't same", ErrorCode.WRITER_NOT_SAME);
        }
        comment.update(commentRequestDto.getContent());
    }

    @Transactional
    public void delete(String accessToken, Long commentIdx){
        memberService.IsAccessTokenExpired(accessToken);
        Member member = extracted(accessToken);
        Comment comment = commentRepository.findById(commentIdx)
                .orElseThrow(() -> new RuntimeException());
        if(member != comment.getWriter()){
            throw new WriterNotSameException("Writer isn't same", ErrorCode.WRITER_NOT_SAME);
        }
        commentRepository.delete(comment);
    }

    private Member extracted(String accessToken) {
        String userEmail = memberService.getUserEmail(accessToken);
        return memberService.findMemberByEmail(userEmail);
    }
}
