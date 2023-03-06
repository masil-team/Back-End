package com.masil.domain.post.dto;

import com.masil.domain.member.dto.response.MemberResponse;
import com.masil.domain.post.entity.Post;
import com.masil.domain.postFile.entity.PostFile;
import com.masil.domain.storage.dto.FileResponse;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
public class PostsElementResponse {
    private Long id;
    private MemberResponse member;
    private Long boardId;
    private String address;
    private String content;
    private int viewCount;
    private int likeCount;
    private int commentCount;
    private Boolean isOwner;
    private Boolean isLiked;
    private Boolean isScrap;
    private LocalDateTime createDate;
    private LocalDateTime modifyDate;
    private FileResponse thumbnail;

    public static PostsElementResponse of(Post post) {

        FileResponse thumbnail = null;
        List<PostFile> postFiles = post.getPostFiles();

        if (!postFiles.isEmpty())
            thumbnail = FileResponse.of(postFiles.get(0));

        return PostsElementResponse.builder()
                .id(post.getId())
                .member(MemberResponse.of(post.getMember()))
                .boardId(post.getBoard().getId())
                .address(post.getEmdAddress().getEmdName())
                .content(post.getPostPreview())
                .viewCount(post.getViewCount())
                .likeCount(post.getLikeCount())
                .commentCount(post.getCommentCount())
                .isOwner(post.getIsOwner())
                .isLiked(post.getIsLiked())
                .isScrap(post.getIsScrap())
                .createDate(post.getCreateDate())
                .modifyDate(post.getModifyDate())
                .thumbnail(thumbnail)
                .build();
    }
}
