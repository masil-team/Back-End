package com.masil.domain.comment.dto;

import com.masil.domain.comment.entity.Comment;
import com.masil.domain.member.entity.Member;
import com.masil.domain.post.entity.Post;
import com.masil.domain.post.entity.State;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ChildrenCreateRequest {

    private String content;

    public ChildrenCreateRequest(String content) {
        this.content = content;
    }

    public Comment toEntity(Post post, Comment comment, Member member){
        return Comment.builder()
                .content(content)
                .post(post)
                .parent(comment)
                .state(State.NORMAL)
                .member(member)
                .build();
    }
}
