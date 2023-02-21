package com.masil.domain.comment.dto;


import com.masil.domain.comment.entity.Comment;
import com.masil.domain.member.dto.response.MemberResponse;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString
@Slf4j
public class CommentResponse {

    private Long id;
    private String content;
    private Long postId;
    private MemberResponse member;
    private Boolean isOwner;
    private int likeCount;
    private Boolean isLiked;
    private LocalDateTime createDate;
    private LocalDateTime modifyDate;

    private Boolean isDeleted; // 자식이 있을경우 삭제되면 true, 삭제 안됐을 경우 false
    private List<ChildrenResponse> replies;

    @Builder
    public CommentResponse(Long id, Long postId, String content, MemberResponse member, Boolean isOwner, int likeCount, Boolean isLiked, LocalDateTime createDate, LocalDateTime modifyDate, Boolean isDeleted, List<ChildrenResponse> replies) {
        this.id = id;
        this.postId = postId;
        this.content = content;
        this.member = member;
        this.isOwner = isOwner;
        this.likeCount = likeCount;
        this.isLiked = isLiked;
        this.createDate = createDate;
        this.modifyDate = modifyDate;
        this.isDeleted = isDeleted;
        this.replies = replies;
    }

    public static CommentResponse of(Comment comment) {
        List<Comment> children = comment.getChildren(); // comment에서 children 을 받아온다.
        log.info("comment = {}", comment.getId());
        List<ChildrenResponse> replies = children.stream()
                .map(ChildrenResponse::of)
                .collect(Collectors.toList());

        return CommentResponse.builder()
                .id(comment.getId())
                .postId(comment.getPost().getId())
                .content(comment.getContent())
                .member(MemberResponse.of(comment.getMember()))
                .isOwner(comment.getIsOwner())
                .likeCount(comment.getLikeCount())
                .isLiked(comment.getIsLiked())
                .isDeleted(comment.isNotAvailable())
                .createDate(comment.getCreateDate())
                .modifyDate(comment.getModifyDate())
                .replies(replies)
                // v2
//                .replies(ChildrenResponse.ofList(comment.getChildren()))
                .build();
    }
}
