package com.masil.domain.commentlike.dto;

import com.masil.domain.postlike.dto.PostLikeResponse;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CommentLikeResponse {
    private int likeCount;
    private Boolean status; // true = 좋아요, false = 좋아요 취소

    public static CommentLikeResponse of(int likeCount, boolean status) {
        return new CommentLikeResponse(likeCount, status);
    }
}
