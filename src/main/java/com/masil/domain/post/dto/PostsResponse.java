package com.masil.domain.post.dto;

import com.masil.domain.bookmark.entity.Bookmark;
import com.masil.domain.post.entity.Post;
import com.masil.domain.postlike.entity.PostLike;
import lombok.Getter;
import org.springframework.data.domain.Slice;

import java.util.List;
import java.util.stream.Collectors;

@Getter
public class PostsResponse {

    private List<PostsElementResponse> posts;
    private Boolean isLast;

    public PostsResponse(List<PostsElementResponse> posts, boolean isLast) {
        this.posts = posts;
        this.isLast = isLast;
    }

    public static PostsResponse ofPosts(Slice<Post> posts) {
        List<PostsElementResponse> postsResponse = posts
                .stream()
                .map(post -> PostsElementResponse.of(post))
                .collect(Collectors.toList());

        return new PostsResponse(postsResponse, posts.isLast());
    }

    public static PostsResponse ofBookmarks(Slice<Bookmark> bookmarks) {
        List<PostsElementResponse> postsResponse = bookmarks
                .stream()
                .map(bookmark -> PostsElementResponse.of(bookmark.getPost()))
                .collect(Collectors.toList());

        return new PostsResponse(postsResponse, bookmarks.isLast());
    }

    public static PostsResponse ofPostLikes(Slice<PostLike> postLikes) {
        List<PostsElementResponse> postsResponse = postLikes
                .stream()
                .map(postLike -> PostsElementResponse.of(postLike.getPost()))
                .collect(Collectors.toList());

        return new PostsResponse(postsResponse, postLikes.isLast());
    }
}
