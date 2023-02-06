package com.masil.domain.member.dto.request;

import lombok.Builder;
import lombok.Getter;

@Getter
public class MemberAddressRequest {

    private Integer rCode;

    @Builder
    public MemberAddressRequest(Integer rCode) {
        this.rCode = rCode;
    }
}
