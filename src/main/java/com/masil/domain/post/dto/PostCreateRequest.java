package com.masil.domain.post.dto;

import com.masil.domain.board.entity.Board;
import com.masil.domain.member.entity.Member;
import com.masil.domain.post.entity.Post;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@NoArgsConstructor
public class PostCreateRequest {

    @NotBlank(message = "본문이 없습니다.")
    private String content;

    @NotNull(message = "카테고리를 선택해주세요.")
    private Long boardId;

    @Builder
    public PostCreateRequest(String content, Long boardId){
        this.content = content;
        this.boardId = boardId;
    }

    public Post toEntity(Member member, Board board){
        return Post.builder()
                .member(member)
                .content(content)
                .board(board)
                .emdAddress(member.getEmdAddress())
                .build();
    }
}