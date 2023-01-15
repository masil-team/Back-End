package com.masil.domain.post.dto;

import com.masil.domain.comment.dto.CommentResponse;
import com.masil.domain.post.entity.Post;
import lombok.Builder;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class PostResponse {

    private Long id;
    private String nickname;
    private String content;
    private Long viewCount;
    private List<CommentResponse> comments = new ArrayList<>();

    @Builder
    private PostResponse(Long id, String nickname, String content, Long viewCount,  List<CommentResponse> commentsResponse) {
        this.id = id;
        this.nickname = nickname;
        this.content = content;
        this.comments = commentsResponse;
        this.viewCount = viewCount;
    }

    public static PostResponse from(Post post) {
        return PostResponse.builder()
                .id(post.getId())
                .nickname(post.getUser().getNickname())
                .content(post.getContent())
                .viewCount(post.getViewCount())
                .build();
    }
}
