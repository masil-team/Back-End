package com.masil.domain.member.dto.request;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class MemberAddressRequest {

    private Integer emdId;

    @Builder
    public MemberAddressRequest(Integer emdId) {
        this.emdId = emdId;
    }
}
