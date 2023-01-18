package com.masil.domain.comment.dto;


import com.masil.domain.comment.entity.Comment;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString
public class CommentResponse {

    private Long id;
    private String content;
    private Long postId;
    private String nickname;

    @Builder
    public CommentResponse(Long id, Long postId, String content, String nickname) {
        this.id = id;
        this.postId = postId;
        this.content = content;
        this.nickname = nickname;
    }

    public static CommentResponse createCommentDto(Comment comment) {
        return CommentResponse.builder()
                .id(comment.getId())
                .postId(comment.getPost().getId())
                .content(comment.getContent())
                .nickname(comment.getMember().getNickname())
                .build();
    }
    
    /*
    public static CommentResponse commentResponse(Comment comment) {
        return CommentResponse.builder()
                .id(comment.getId())
                .nickname(comment.getMember().getNickname())
                .content(comment.getContent())
                .build();
    }
    */
}
