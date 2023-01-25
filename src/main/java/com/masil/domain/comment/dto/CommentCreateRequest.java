package com.masil.domain.comment.dto;

import com.masil.domain.comment.entity.Comment;
import com.masil.domain.member.entity.Member;
import com.masil.domain.post.entity.Post;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Getter
@NoArgsConstructor
public class CommentCreateRequest {

    @NotBlank(message = "댓글을 작성하지 않았습니다.")
    private String content;


    @Builder
    public CommentCreateRequest(String content) {
        this.content = content;
    }

    //dto -> toEntity
    public Comment toEntity(Post post/* Member member*/){
        return Comment.builder()
                .content(content)
                .post(post)
//                .member(member)
                .build();
    }
}
