package com.masil.domain.comment.dto;


import com.masil.domain.comment.entity.Comment;
import lombok.*;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString
public class CommentResponse {

    private Long id;
    private String content;
    private Long postId;
    private String nickname;
    private Long memberId;
    private int likeCount;
    private LocalDateTime createDate;
    private LocalDateTime modifyDate;

    @Builder
    public CommentResponse(Long id, Long postId, String content, String nickname, Long memberId, int likeCount, LocalDateTime createDate, LocalDateTime modifyDate) {
        this.id = id;
        this.postId = postId;
        this.content = content;
        this.nickname = nickname;
        this.memberId = memberId;
        this.likeCount = likeCount;
        this.createDate = createDate;
        this.modifyDate = modifyDate;
    }

    public static CommentResponse createCommentDto(Comment comment) {
        return CommentResponse.builder()
                .id(comment.getId())
                .postId(comment.getPost().getId())
                .content(comment.getContent())
                .nickname(comment.getMember().getNickname())
                .memberId(comment.getMember().getId())
                .likeCount(comment.getLikeCount())
                .createDate(comment.getCreateDate())
                .modifyDate(comment.getModifyDate())
                .build();
    }
}
