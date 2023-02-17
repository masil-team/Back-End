package com.masil.domain.address.dto.response;

import com.masil.domain.address.entity.EmdAddress;
import lombok.Builder;
import lombok.Getter;

@Getter
public class AddressResponse {

    private Integer emdId;

    private String emdName;

    @Builder
    public AddressResponse(Integer emdId, String emdName) {
        this.emdId = emdId;
        this.emdName = emdName;
    }

    public static AddressResponse of(EmdAddress emdAddress) {
        return AddressResponse.builder()
                .emdId(emdAddress.getId())
                .emdName(emdAddress.getEmdName())
                .build();
    }
}
