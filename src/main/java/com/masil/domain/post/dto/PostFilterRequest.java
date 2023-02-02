package com.masil.domain.post.dto;

import lombok.Getter;
import org.springframework.data.domain.Pageable;


@Getter
public class PostFilterRequest {

    private Long boardId;

    private Integer rCode;

    private Pageable pageable;

    public PostFilterRequest(Long boardId , Integer rCode, Pageable pageable) {
        this.boardId = boardId;
        this.rCode = rCode;
        this.pageable = pageable;
    }

    public boolean isEmdAddress() {
        return String.valueOf(rCode).length() > 5;
    }
}
