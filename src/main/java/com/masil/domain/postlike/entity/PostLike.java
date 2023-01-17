package com.masil.domain.postlike.entity;

import com.masil.domain.post.entity.Post;
import com.masil.domain.member.entity.Member;
import com.masil.global.common.entity.BaseEntity;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@RequiredArgsConstructor
public class PostLike extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

}
