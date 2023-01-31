package com.masil.domain.post.entity;

import com.masil.domain.board.entity.Board;
import com.masil.domain.comment.entity.Comment;
import com.masil.domain.member.entity.Member;
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

    @Column(columnDefinition = "integer default 0", nullable = false)
    private int viewCount;

    @Column(columnDefinition = "integer default 0", nullable = false)
    private int likeCount;

    @Builder.Default
    @Enumerated(EnumType.STRING)
    private State state = State.NORMAL;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "board_id")
    private Board board;

    @Builder.Default
    @OneToMany(mappedBy = "post")
    private List<Comment> comments = new ArrayList<>();

    @Builder
    private Post(String content, Member member, State state, Board board) {
        this.content = content;
        this.member = member;
        this.state = state;
        this.board = board;
    }
    
    public void updateContentAndBoard(String content, Board board){
        this.content = content;
        this.board = board;
    }

    public void tempDelete() {
        this.state = State.DELETE;
    }

    public boolean isOwner(Long memberId) {
        return this.member.getId() == memberId;
    }

    public void plusView() {
        this.viewCount++;
    }
    public void plusLike() {
        this.likeCount++;
    }
    public void minusLike() {
        this.likeCount--;
    }
    public int getCommentCount() {
        if (comments == null) {
            return 0;
        }
        return comments.size();
    }

    public boolean isDeleted() {
        return this.state == State.DELETE;
    }
}
