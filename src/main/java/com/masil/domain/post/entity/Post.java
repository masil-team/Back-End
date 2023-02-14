package com.masil.domain.post.entity;

import com.masil.domain.address.entity.EmdAddress;
import com.masil.domain.board.entity.Board;
import com.masil.domain.comment.entity.Comment;
import com.masil.domain.member.entity.Member;
import com.masil.global.common.entity.BaseEntity;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

import static javax.persistence.FetchType.LAZY;

@Entity
@Getter
@SuperBuilder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Where(clause = "state = 'NORMAL'")
public class Post extends BaseEntity {

    private static final int PREVIEW_LENGTH = 400;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "TEXT")
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

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "emdAddress_id")
    private EmdAddress emdAddress;

    @Builder.Default
    @OneToMany(mappedBy = "post")
    private List<Comment> comments = new ArrayList<>();

    @Builder.Default
    @Transient
    private Boolean isOwner = false;

    @Builder.Default
    @Transient
    private Boolean isLiked = false;

    @Builder
    private Post(String content, Member member, State state, Board board , EmdAddress emdAddress) {
        this.content = content;
        this.member = member;
        this.state = state;
        this.board = board;
        this.emdAddress = emdAddress;
    }
    
    public void updateContentAndBoard(String content, Board board){
        this.content = content;
        this.board = board;

    }
    public void updatePostPermissions(Boolean isOwner, Boolean isLiked){
        this.isOwner = isOwner;
        this.isLiked = isLiked;
    }

    public void tempDelete() {
        this.state = State.DELETE;
    }

    public boolean isOwner(Long memberId) {
        return this.member.getId() == memberId;
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
    public String getPostPreview() {
        return this.content.substring(0, Math.min(PREVIEW_LENGTH, content.length()));
    }
}
