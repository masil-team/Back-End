package com.masil.domain.post.dto;

import lombok.Getter;
import org.springframework.data.domain.Pageable;


@Getter
public class PostFilterRequest {

    private static final int SGG_ADDRESS_ID_LENGTH = 5;
    private static final int EMD_ADDRESS_ID_LENGTH = 8;

    private Long boardId;

    private Integer rCode;

    private Pageable pageable;

    public PostFilterRequest(Long boardId , Integer rCode, Pageable pageable) {
        this.boardId = boardId;
        this.rCode = rCode;
        this.pageable = pageable;
    }

    public boolean isEmdAddress() {
        return String.valueOf(rCode).length() > SGG_ADDRESS_ID_LENGTH;
    }
}
