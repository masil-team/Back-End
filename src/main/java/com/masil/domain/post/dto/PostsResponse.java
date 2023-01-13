package com.masil.domain.post.dto;

import com.masil.domain.post.entity.Post;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
public class PostsResponse {

    private List<PostResponse> posts;

    public PostsResponse(List<PostResponse> posts) {
        this.posts = posts;
    }
    public static PostsResponse ofPosts(List<Post> postList) {
        // post 리스트 -> postResponse 리스트
        List<PostResponse> postsResponse = postList
                .stream()
                .map(PostResponse::from)
                .collect(Collectors.toList());

        return new PostsResponse(postsResponse);
    }
}
