package com.masil.domain.post.dto;

import com.masil.domain.post.entity.Post;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class PostsElementResponse {
    private Long id;
    private Long memberId;
    private String nickname;
    private String content;  // 글자 제한
    private int viewCount;
    private int likeCount;
    private int commentCount;
//    @Builder.Default
//    private Boolean isOwner = false;
//    @Builder.Default
//    private Boolean isLike = false;
//    private Long boardId;
    private LocalDateTime createDate;
    private LocalDateTime modifyDate;

    public static PostsElementResponse of(Post post) {
        return PostsElementResponse.builder()
                .id(post.getId())
                .memberId(post.getMember().getId())
                .nickname(post.getMember().getNickname())
                .content(post.getContent())
                .viewCount(post.getViewCount())
                .likeCount(post.getLikeCount())
                .commentCount(post.getCommentCount())
//                .isOwner(isOwner)
//                .isLike(isLike)
                .createDate(post.getCreateDate())
                .modifyDate(post.getModifyDate())
                .build();
    }
}
