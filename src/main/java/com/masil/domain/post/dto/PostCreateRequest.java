package com.masil.domain.post.dto;

import com.masil.domain.post.entity.Post;
import com.masil.domain.member.entity.Member;
import com.masil.domain.post.entity.State;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Getter
@NoArgsConstructor
public class PostCreateRequest {

    @NotBlank(message = "본문이 없습니다.")
    private String content;

    @Builder
    public PostCreateRequest(String content){
        this.content = content;
    }

    public Post toEntity(Member member){
        return Post.builder()
                .member(member)
                .content(content)
                .state(State.NORMAL)
                .build();
    }
}