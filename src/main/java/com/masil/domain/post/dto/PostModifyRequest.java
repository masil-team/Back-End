package com.masil.domain.post.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Getter
@NoArgsConstructor
public class PostModifyRequest {

    @NotBlank(message = "본문이 없습니다.")
    private String content;

    @Builder
    public PostModifyRequest(String content){
        this.content = content;
    }
}
