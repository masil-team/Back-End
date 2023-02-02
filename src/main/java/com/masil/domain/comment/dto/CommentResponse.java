package com.masil.domain.comment.dto;


import com.masil.domain.comment.entity.Comment;
import com.masil.domain.member.dto.response.MemberResponse;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString
public class CommentResponse {

    private Long id;
    private String content;
    private Long postId;
    private MemberResponse member;
    private int likeCount;
    private LocalDateTime createDate;
    private LocalDateTime modifyDate;
    private List<ChildrenResponse> replies;

    @Builder
    public CommentResponse(Long id, Long postId, String content, MemberResponse member, int likeCount, LocalDateTime createDate, LocalDateTime modifyDate, List<ChildrenResponse> replies) {
        this.id = id;
        this.postId = postId;
        this.content = content;
        this.member = member;
        this.likeCount = likeCount;
        this.createDate = createDate;
        this.modifyDate = modifyDate;
        this.replies = replies;
    }

    public static CommentResponse responseCommentDto(Comment comment) {
        List<Comment> children = comment.getChildren(); // comment에서 children 을 받아온다.
        List<ChildrenResponse> replies = children.stream()
                .map(ChildrenResponse::of)
                .distinct()
                .collect(Collectors.toList());

        return CommentResponse.builder()
                .id(comment.getId())
                .postId(comment.getPost().getId())
                .content(comment.getContent())
                .member(MemberResponse.of(comment.getMember()))
                .likeCount(comment.getLikeCount())
                .createDate(comment.getCreateDate())
                .modifyDate(comment.getModifyDate())
                .replies(replies)
                // v2
//                .replies(ChildrenResponse.ofList(comment.getChildren()))
                .build();
    }
}
