package com.management.diet.util;

import com.management.diet.domain.Member;
import com.management.diet.exception.ErrorCode;
import com.management.diet.exception.exception.MemberNotFindException;
import com.management.diet.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CurrentMemberUtil {

    private final MemberRepository memberRepository;

    public static String getCurrentEmail(){
        String email = null;
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if(principal instanceof UserDetails) {
            email = ((Member) principal).getEmail();
        } else{
            email = principal.toString();
        }
        return email;
    }

    public Member getCurrentMember() {
        String email = null;
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if(principal instanceof UserDetails) {
            email = ((Member) principal).getEmail();
        } else{
            email = principal.toString();
        }
        return memberRepository.findByEmail(email)
                .orElseThrow(() -> new MemberNotFindException("Member can't find", ErrorCode.MEMBER_NOT_FIND));
    }
}
