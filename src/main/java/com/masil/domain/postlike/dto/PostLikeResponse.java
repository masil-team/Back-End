package com.masil.domain.postlike.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PostLikeResponse {
    private int likeCount;
    private Boolean isLike;

    public static PostLikeResponse of(int likeCount, boolean isLike) {
        return new PostLikeResponse(likeCount, isLike);
    }
}
