package com.masil.domain.comment.dto;

import com.masil.domain.comment.entity.Comment;
import lombok.*;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.stream.Collectors;

@NoArgsConstructor
@Getter
@ToString
public class CommentsResponse {
    private List<CommentResponse> comments;
    private Long totalCommentCount;
    private int totalPage;

    @Builder
    public CommentsResponse(List<CommentResponse> comments, Long totalCommentCount, int totalPage) {
        this.comments = comments;
        this.totalCommentCount = totalCommentCount;
        this.totalPage = totalPage;
    }

    public static CommentsResponse ofComment(Page<Comment> comment, Long totalCommentCount) {
        List<CommentResponse> comments = comment.stream()
                .map(CommentResponse::of)
                .collect(Collectors.toList());


        return new CommentsResponse(comments, totalCommentCount, comment.getTotalPages());
    }
}
