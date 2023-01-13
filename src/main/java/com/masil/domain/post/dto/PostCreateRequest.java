package com.masil.domain.post.dto;

import com.masil.domain.post.entity.Post;
import com.masil.domain.user.entity.User;
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

    public Post toEntity(User user){
        return Post.builder()
                .user(user)
                .content(content)
                .build();
    }
}