package com.masil.domain.fixture;

import com.masil.domain.board.entity.Board;
import com.masil.domain.member.entity.Member;

public enum BoardFixture {
    전체_카테고리(1L, "전체"),
    동네소식_카테고리(2L, "동네소식"),
    동네질문_카테고리(3L, "동네질문"),
    일상_카테고리(4L, "일상"),
    분실_실종_카테고리(5L, "분실/실종")
    ;

    private final Long id;
    private final String name;

    BoardFixture(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Board 엔티티_생성() {
        return Board.builder()
                .name(this.name)
                .build();
    }
}
