package com.masil.domain.comment.dto;

import com.masil.domain.comment.entity.Comment;
import com.masil.domain.member.entity.Member;
import com.masil.domain.post.entity.Post;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.core.userdetails.User;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class CommentCreateRequest {

    private Long id;
    @NotBlank(message = "댓글을 작성하지 않았습니다.")
    private String content;


    @Builder
    public CommentCreateRequest(Long id, String content) {
        this.id = id;
        this.content = content;
    }

    //dto -> toEntity
    public Comment toEntity(Post post){
        return Comment.builder()
                .id(id)
                .content(content)
                .post(post)
                .build();
    }
}
