package com.masil.domain.post.entity;

import com.masil.domain.comment.entity.Comment;
import com.masil.domain.user.entity.User;
import com.masil.global.common.entity.BaseEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

import static javax.persistence.FetchType.LAZY;

@Entity
@Getter
@SuperBuilder
@RequiredArgsConstructor
public class Post extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String content;
    private Long viewCount;

    @Enumerated(EnumType.STRING)
    private State state;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "USER_ID")
    private User user;
    @OneToMany(mappedBy = "post")
    private List<Comment> comments = new ArrayList<>();

    @Builder
    private Post(String content, User user) {
        this.content = content;
        this.user = user;
        this.state = State.NORMAL;
    }
    
    public void updateContent(String content){
        this.content = content;
    }

    public void tempDelete() {
        this.state = State.DELETE;
    }

}
