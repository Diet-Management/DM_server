package com.management.diet.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.management.diet.dto.request.MemberRequestDto;
import com.management.diet.dto.request.PostingRequestDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Entity
@Builder
@Getter
@AllArgsConstructor @NoArgsConstructor
public class Posting {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "posting_id")
    private Long postingIdx;

    @Column(name = "title")
    private String title;

    @Column(name = "content")
    private String content;

    @Column(name = "date")
    private LocalDate date;

    @Column(name = "fix")
    private Boolean fix;

    @Column(name = "goods")
    private int goods;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @OneToMany(cascade = CascadeType.REMOVE, mappedBy = "posting")
    private List<Comment> comments;

    public void update(PostingRequestDto postingRequestDto){
        this.title=postingRequestDto.getTitle();
        this.content=postingRequestDto.getContent();
        this.date=LocalDate.now();
        this.fix=true;
    }

    public void updateGoods(int goods){
        if(goods<0){
            throw new RuntimeException();
        }
        this.goods=goods;
    }
}
