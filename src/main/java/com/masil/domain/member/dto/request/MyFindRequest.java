package com.masil.domain.member.dto.request;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class MyFindRequest {
    private Long memberId;

    @Builder
    public MyFindRequest(Long memberId) {
        this.memberId = memberId;
    }
}
