package com.masil.domain.post.entity;

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

    @Enumerated(EnumType.STRING)
    private State state;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @Builder.Default
    @OneToMany(mappedBy = "post")
    private List<Comment> comments = new ArrayList<>();

    @Builder
    private Post(String content, Member member) {
        this.content = content;
        this.member = member;
        this.state = State.NORMAL;
    }
    
    public void updateContent(String content){
        this.content = content;
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
}
