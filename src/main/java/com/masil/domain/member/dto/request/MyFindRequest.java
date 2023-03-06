package com.masil.domain.member.dto.request;

import lombok.Builder;
import lombok.Getter;

@Getter
public class MyFindRequest {
    private final Long memberId;

    @Builder
    public MyFindRequest(Long memberId) {
        this.memberId = memberId;
    }
}
