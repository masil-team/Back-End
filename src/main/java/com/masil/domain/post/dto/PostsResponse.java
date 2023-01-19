package com.masil.domain.post.dto;

import com.masil.domain.post.entity.Post;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
public class PostsResponse {

    private List<PostsElementResponse> posts;

    public PostsResponse(List<PostsElementResponse> posts) {
        this.posts = posts;
    }
    public static PostsResponse ofPosts(List<Post> posts) {
        List<PostsElementResponse> postsResponse = posts
                .stream()
                .map((Post post) -> PostsElementResponse.of(post))
                .collect(Collectors.toList());

        return new PostsResponse(postsResponse);
    }
}
