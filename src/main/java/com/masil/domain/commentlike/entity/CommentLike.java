package com.masil.domain.commentlike.entity;

import com.masil.domain.comment.entity.Comment;
import com.masil.domain.member.entity.Member;
import com.masil.global.common.entity.BaseEntity;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@RequiredArgsConstructor
public class CommentLike extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "comment_id")
    private Comment post;
}
