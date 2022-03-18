package com.management.diet.service;

import com.management.diet.domain.Comment;
import com.management.diet.domain.Member;
import com.management.diet.domain.Posting;
import com.management.diet.dto.request.CommentRequestDto;
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
    public Long writeComment(CommentRequestDto commentRequestDto, Long postingIdx,String accessToken){
        String userEmail = memberService.getUserEmail(accessToken);
        Member member = memberService.findMemberByEmail(userEmail);
        Comment comment = commentRequestDto.toEntity(member);
        Posting posting = postingService.getPostingByIdx(postingIdx);
        posting.getComments().add(comment);
        return commentRepository.save(comment).getCommentIdx();
    }

}
