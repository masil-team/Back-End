package com.masil.domain.post.dto;

import com.masil.domain.board.entity.Board;
import com.masil.domain.member.entity.Member;
import com.masil.domain.post.entity.Post;
import com.masil.domain.postFile.entity.PostFile;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;


@Getter
@NoArgsConstructor
public class PostCreateRequest {

    @NotBlank(message = "본문이 없습니다.")
    private String content;

    @NotNull(message = "카테고리를 선택해주세요.")
    private Long boardId;

    private List<Long> fileIds = new ArrayList<>();

    @Builder
    public PostCreateRequest(String content, Long boardId, List<Long> fileIds){
        this.content = content;
        this.boardId = boardId;
        this.fileIds = fileIds;
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