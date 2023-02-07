package com.masil.domain.comment.entity;

import com.masil.domain.comment.exception.CommentInputException;
import com.masil.domain.commentlike.entity.CommentLike;
import com.masil.domain.member.entity.Member;
import com.masil.domain.post.entity.Post;
import com.masil.domain.post.entity.State;
import com.masil.global.common.entity.BaseEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

import static javax.persistence.FetchType.LAZY;

@Getter
@SuperBuilder
@Entity
@RequiredArgsConstructor
@Where(clause = "state = 'NORMAL'")
public class Comment extends BaseEntity {

    private static final int MAX_LENGTH = 400;

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

    /**
     * 부모 댓글과 자식 추가
     * 셀프 조인
     */
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "parent_id")
    private Comment parent;

    @Builder.Default
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

    /**
     * 좋아요 여부, 오너
     */
    @Builder.Default
    @Transient
    private Boolean isOwner = false;

    @Builder.Default
    @Transient
    private Boolean isLiked = false;

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

    /**
     * soft delete
     */
    public void tempDelete() {
        this.state = State.DELETE;
    }

    /**
     * 글자 수 초과 exception
     */
    public void validateLength(String content) {
        if (content.length() > MAX_LENGTH) {
            throw new CommentInputException();
        }
    }

    /**
     * 자신이 맞는지 알아보는 로직
     */
    public boolean isOwner(Long memberId) {
        return this.member.getId() == memberId;
    }

    public boolean isCommentWriter(Long memberId){
        return this.member.getId() == (memberId);
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

    /**
     * isOwner와 isLiked를 true로 만들어주는 로직
     */

    public void updateIsCommentWriter(Boolean isOwner) {
        this.isOwner = isOwner;
    }

    public void updateCommentLiked(Boolean isLiked){
        this.isLiked = isLiked;
    }
}
