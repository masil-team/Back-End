package com.masil.domain.post.entity;

import com.masil.domain.comment.entity.Comment;
import com.masil.domain.user.entity.User;
import com.masil.global.common.entity.BaseEntity;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import javax.persistence.*;

import java.util.ArrayList;
import java.util.List;

import static javax.persistence.FetchType.LAZY;

@Entity
@Getter
@RequiredArgsConstructor
public class Post extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "USER_ID")
    private User user;

    private String content;
    private Long viewCount;
    private State state;

    @OneToMany(mappedBy = "post")
    private List<Comment> comments = new ArrayList<>();
}
