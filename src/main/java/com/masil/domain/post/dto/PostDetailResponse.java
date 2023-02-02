package com.masil.domain.post.dto;

import com.masil.domain.member.dto.response.MemberResponse;
import com.masil.domain.post.entity.Post;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class PostDetailResponse {

    private Long id;
    private MemberResponse member;
    private Long boardId;
    private String address;
    private String content;
    private int viewCount;
    private int likeCount;
    private Boolean isOwner;
    private Boolean isLiked;
    private LocalDateTime createDate;
    private LocalDateTime modifyDate;

    public static PostDetailResponse of(Post post) {
        return PostDetailResponse.builder()
                .id(post.getId())
                .member(MemberResponse.of(post.getMember()))
                .boardId(post.getBoard().getId())
                .address(post.getEmdAddress().getEmdName())
                .content(post.getContent())
                .viewCount(post.getViewCount())
                .likeCount(post.getLikeCount())
                .isOwner(post.getIsOwner())
                .isLiked(post.getIsLiked())
                .createDate(post.getCreateDate())
                .modifyDate(post.getModifyDate())
                .build();
    }

}
