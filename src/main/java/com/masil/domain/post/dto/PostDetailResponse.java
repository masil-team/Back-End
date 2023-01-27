package com.masil.domain.post.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.masil.domain.member.dto.response.MemberResponse;
import com.masil.domain.post.entity.Post;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.Column;
import java.time.LocalDateTime;

@Getter
@Builder
public class PostDetailResponse {

    private Long id;
    private MemberResponse member;
    private String content;
    private int viewCount;
    private int likeCount;
    private Boolean isOwner;
    private Boolean isLike;
    private LocalDateTime createDate;
    private LocalDateTime modifyDate;

    public static PostDetailResponse of(Post post, boolean isOwner, boolean isLike) {
        return PostDetailResponse.builder()
                .id(post.getId())
                .member(MemberResponse.of(post.getMember()))
                .content(post.getContent())
                .viewCount(post.getViewCount())
                .likeCount(post.getLikeCount())
                .isOwner(isOwner)
                .isLike(isLike)
                .createDate(post.getCreateDate())
                .modifyDate(post.getModifyDate())
                .build();
    }
    public static PostDetailResponse of(Post post) {
        return PostDetailResponse.of(post, false, false);
    }
}
