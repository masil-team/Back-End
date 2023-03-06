package com.masil.domain.member.dto.response;

import com.masil.domain.post.dto.PostsResponse;
import lombok.Getter;

@Getter
public class MyFindResponse {

    private Long memberId;
    private PostsResponse myPostsResponse;

    public MyFindResponse(Long memberId, PostsResponse myPostsResponse) {
        this.memberId = memberId;
        this.myPostsResponse = myPostsResponse;
    }

}
