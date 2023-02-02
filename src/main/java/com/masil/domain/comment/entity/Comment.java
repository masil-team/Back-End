package com.masil.domain.comment.entity;

import com.masil.domain.commentlike.entity.CommentLike;
import com.masil.domain.member.entity.Member;
import com.masil.domain.post.entity.Post;
import com.masil.domain.post.entity.State;
import com.masil.global.common.entity.BaseEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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

//    private Integer depth; // 댓글 depth

    /**
     * 부모 댓글과 자식 추가
     * 셀프 조인
     */
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "parent_id")
    private Comment parent;

//    @Builder.Default
    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL)
    private List<Comment> children = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    private State state;

    /**
     * commentLikes 추가
     */
    @OneToMany(mappedBy = "comment", cascade = CascadeType.PERSIST, orphanRemoval = true)
    private List<CommentLike> commentLikes = new ArrayList<>();

    @Column(columnDefinition = "integer default 0", nullable = false)
    private int likeCount;

    @Builder
    private Comment(String content, Member member, Post post, State state, Comment parent) {
        this.content = content;
        this.member = member;
        this.post = post;
        this.state = state;
        this.parent = parent;
    }

    public void updateContent(String content) {
        this.content = content;
    }

    public void tempDelete() {
        this.state = State.DELETE;
    }

    /**
     * 자신이 맞는지 알아보는 로직
     */
    public boolean isOwner(Long memberId) {
        return this.member.getId() == memberId;
    }

    /**
     * 댓글 좋아요 기능
     */
    public void increaseLikeCount() {
        this.likeCount += 1;
    }

    public void decreaseLikeCount() {
        this.likeCount -= 1;
    }
}
