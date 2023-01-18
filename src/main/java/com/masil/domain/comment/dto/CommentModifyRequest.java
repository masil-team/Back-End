package com.masil.domain.comment.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Getter
@NoArgsConstructor
public class CommentModifyRequest {

    @NotBlank(message = "댓글을 작성하지 않았습니다.")
    private String content;

    @Builder
    public CommentModifyRequest(String content){
        this.content = content;
    }
}
