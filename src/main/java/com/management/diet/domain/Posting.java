package com.management.diet.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Builder
@Getter
@AllArgsConstructor @NoArgsConstructor
public class Posting {
    @Id
    @GeneratedValue @Column(name = "posting_id")
    private Long postingIdx;

    @Column(name = "title")
    private String title;

    @Column(name = "content")
    private String content;

    @Column(name = "date")
    LocalDate date;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;
}
