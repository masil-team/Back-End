package com.masil.domain.comment.dto;

import com.masil.domain.comment.entity.Comment;
import com.masil.domain.member.entity.Member;
import com.masil.domain.post.entity.Post;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.userdetails.User;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class CommentCreateRequest {

    private Long id;
    @NotBlank(message = "댓글을 작성하지 않았습니다.")
    private String content;
    private LocalDateTime createdDate;
    private LocalDateTime modifiedDate;
    private Member member;
    private Post post;


    public CommentCreateRequest(Long id, String content, LocalDateTime createdDate, LocalDateTime modifiedDate, Member member, Post post) {
        this.id = id;
        this.content = content;
        this.createdDate = createdDate;
        this.modifiedDate = modifiedDate;
        this.member= member;
        this.post = post;
    }

    //dto -> toEntity
    public Comment toEntity(){
        return Comment.builder()
                .id(id)
                .content(content)
                .member(member)
                .post(post)
                .build();
    }
}
