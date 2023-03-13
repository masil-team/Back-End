package com.masil.domain.member.dto.response;

import com.masil.domain.comment.dto.CommentsResponse;
import com.masil.domain.post.dto.PostsResponse;
import lombok.Builder;

public class LikeFindResponse {
    private Long memberId;
    private PostsResponse posts;
    private CommentsResponse comments;

    @Builder
    public LikeFindResponse(Long memberId, PostsResponse posts, CommentsResponse comments) {
        this.memberId = memberId;
        this.posts = posts;
        this.comments = comments;
    }
}
