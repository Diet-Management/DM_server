package com.management.diet.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Builder
@Getter
@AllArgsConstructor @NoArgsConstructor
public class Comment {
    @Id
    @GeneratedValue @Column(name = "comment_id")
    private Long commentIdx;

    @Column(name = "content")
    private String content;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member writer;
}
