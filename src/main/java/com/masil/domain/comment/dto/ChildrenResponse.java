package com.masil.domain.comment.dto;

import com.masil.domain.comment.entity.Comment;
import com.masil.domain.member.dto.response.MemberResponse;
import com.masil.domain.member.entity.Member;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

//@AllArgsConstructor
//@NoArgsConstructor
//@ToString
@Getter
@Builder
public class ChildrenResponse {
    private Long id;
    private String content;
    private Long postId;
    private MemberResponse member;
    private int likeCount;
    private LocalDateTime createDate;
    private LocalDateTime modifyDate;

    public static ChildrenResponse of(Comment comment) {
        return ChildrenResponse.builder()
                .id(comment.getId())
                .content(comment.getContent())
                .postId(comment.getPost().getId())
                .member(MemberResponse.of(comment.getMember()))
                .likeCount(comment.getLikeCount())
                .createDate(comment.getCreateDate())
                .modifyDate(comment.getModifyDate())
                .build();
    }

    // v2
//    public static List<ChildrenResponse> ofList(List<Comment> childList) {
//        return childList.stream()
//                .map(ChildrenResponse::of)
//                .collect(Collectors.toList());
//    }
}
