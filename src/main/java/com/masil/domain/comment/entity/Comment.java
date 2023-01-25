package com.masil.domain.comment.entity;

import com.masil.domain.member.entity.Member;
import com.masil.domain.post.entity.Post;
import com.masil.domain.post.entity.State;
import com.masil.global.common.entity.BaseEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.security.core.userdetails.User;

import javax.persistence.*;

import java.util.ArrayList;
import java.util.List;

import static javax.persistence.FetchType.LAZY;

@Getter
@SuperBuilder
@Entity
@RequiredArgsConstructor
public class Comment extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "POST_ID", nullable = false)
    private Post post;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "MEMBER_ID")
    private Member member;

    @Column(columnDefinition = "integer default 0", nullable = false)
    private int likeCount;

    /**
     * 부모 댓글과 자식 추가
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private Comment parent;

    @Builder.Default
    @OneToMany(mappedBy = "parent", orphanRemoval = true)
    private List<Comment> children = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    private State state;

    @Builder
    private Comment(Long id, String content, Member member, Post post) {
        this.id = id;
        this.content = content;
        this.member = member;
        this.post = post;
        this.state = State.NORMAL;
    }

    public void updateContent(String content) {
        this.content = content;
    }

    public void tempDelete() {
        this.state = State.DELETE;
    }


    /**
     * 댓글 좋아요 기능
     */
    public void plusLike() {
        this.likeCount++;
    }
    public void minusLike() {
        this.likeCount--;
    }
}
