package com.masil.domain.comment.entity;

import com.masil.domain.post.entity.Post;
import com.masil.domain.post.entity.State;
import com.masil.global.common.entity.BaseEntity;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;

@Getter
@SuperBuilder
@Entity
public class Comment extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    private Post post;

    private State state;

}
