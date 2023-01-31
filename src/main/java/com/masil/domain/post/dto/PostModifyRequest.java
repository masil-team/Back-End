package com.masil.domain.post.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@NoArgsConstructor
public class PostModifyRequest {

    @NotBlank(message = "본문이 없습니다.")
    private String content;

    @NotNull(message = "카테고리를 선택해주세요.")
    private Long boardId;

    @Builder
    public PostModifyRequest(String content, Long boardId){
        this.content = content;
        this.boardId = boardId;
    }
}
