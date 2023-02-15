package com.masil.domain.bookmark.dto;

import com.masil.domain.bookmark.entity.Bookmark;
import com.masil.domain.member.dto.response.MemberResponse;
import com.masil.domain.post.entity.Post;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class BookmarksElementResponse {
    private Long postId;
    private MemberResponse member;
    private Long boardId;
    private String address;
    private String content;
    private LocalDateTime createDate;
    private LocalDateTime modifyDate;

    public static BookmarksElementResponse of(Bookmark bookmark) {
        Post post = bookmark.getPost();
        return BookmarksElementResponse.builder()
                .postId(post.getId())
                .member(MemberResponse.of(post.getMember()))
                .boardId(post.getBoard().getId())
                .address(post.getEmdAddress().getEmdName())
                .content(post.getPostPreview())
                .createDate(post.getCreateDate())
                .modifyDate(post.getModifyDate())
                .build();
    }
}
