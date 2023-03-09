package com.masil.domain.post.dto;

import com.masil.domain.member.dto.response.MemberResponse;
import com.masil.domain.post.entity.Post;
import com.masil.domain.postFile.entity.PostFile;
import com.masil.domain.storage.dto.FileResponse;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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
    private Boolean isScrap;
    private LocalDateTime createDate;
    private LocalDateTime modifyDate;
    private int imageCount;
    private List<FileResponse> files;

    public static PostDetailResponse of(Post post) {
        List<FileResponse> filesResponse = new ArrayList<>();

        for (PostFile postFile : post.getPostFiles()) {
            filesResponse.add(FileResponse.of(postFile));
        }

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
                .isScrap(post.getIsScrap())
                .createDate(post.getCreateDate())
                .modifyDate(post.getModifyDate())
                .imageCount(filesResponse.size())
                .files(filesResponse)
                .build();
    }

}
