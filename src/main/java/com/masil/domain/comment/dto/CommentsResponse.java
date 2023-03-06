package com.masil.domain.comment.dto;

import com.masil.domain.comment.entity.Comment;
import com.masil.domain.commentlike.entity.CommentLike;
import com.masil.domain.post.dto.PostsElementResponse;
import lombok.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Slice;

import java.util.List;
import java.util.stream.Collectors;

@NoArgsConstructor
@Getter
@ToString
public class CommentsResponse {
    private List<CommentResponse> comments;
    private Long totalCommentCount;
    private int totalPage;
    private boolean isLast;

    @Builder
    public CommentsResponse(List<CommentResponse> comments, Long totalCommentCount, int totalPage, boolean isLast) {
        this.comments = comments;
        this.totalCommentCount = totalCommentCount;
        this.totalPage = totalPage;
        this.isLast = isLast;
    }

    public static CommentsResponse ofComment(Page<Comment> comment, Long totalCommentCount, int totalPage) {
        List<CommentResponse> comments = comment.stream()
                .map(CommentResponse::of)
                .collect(Collectors.toList());

        return CommentsResponse.builder()
                .comments(comments)
                .totalCommentCount(totalCommentCount)
                .totalPage(comment.getTotalPages())
                .build();
    }

    public static CommentsResponse ofComment(Slice<Comment> comments) {
        List<CommentResponse> responseComments = comments.stream()
                .map(CommentResponse::of)
                .collect(Collectors.toList());

        return CommentsResponse.builder()
                .comments(responseComments)
                .isLast(comments.isLast())
                .build();
    }

    public static CommentsResponse ofCommentLike(Slice<CommentLike> commentLikes) {
        List<CommentResponse> comments = commentLikes
                .stream()
                .map(commentLike -> CommentResponse.of(commentLike.getComment()))
                .collect(Collectors.toList());

        return CommentsResponse.builder()
                .comments(comments)
                .isLast(commentLikes.isLast())
                .build();
    }
}
