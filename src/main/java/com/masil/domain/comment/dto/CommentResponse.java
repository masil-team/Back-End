package com.masil.domain.comment.dto;


import com.masil.domain.comment.entity.Comment;
import com.masil.domain.member.dto.response.MemberResponse;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString
public class CommentResponse {

    private Long id;
    private String content;
    private Long postId;
    private String nickname;
    private Long memberId;

//    private MemberResponse member;


//    @Builder
//    public CommentResponse(Long id, Long postId, String content, String nickname) {
//        this.id = id;
//        this.postId = postId;
//        this.content = content;
//        this.nickname = nickname;
//    }

    @Builder
    public CommentResponse(Long id, Long postId, String content, String nickname, Long memberId) {
        this.id = id;
        this.postId = postId;
        this.content = content;
        this.nickname = nickname;
        this.memberId = memberId;
    }

    public static CommentResponse createCommentDto(Comment comment) {
        return CommentResponse.builder()
                .id(comment.getId())
                .postId(comment.getPost().getId())
                .content(comment.getContent())
                .nickname(comment.getMember().getNickname())
                .memberId(comment.getMember().getId())
                .build();
    }
}
