package com.masil.domain.address.dto.response;

import com.masil.domain.address.entity.EmdAddress;
import lombok.Builder;
import lombok.Getter;

@Getter
public class AddressResponse {

    private Integer emdId;

    private String sidoName;

    private String sggName;

    private String emdName;

    @Builder
    public AddressResponse(Integer emdId, String sidoName, String sggName, String emdName) {
        this.emdId = emdId;
        this.sidoName = sidoName;
        this.sggName = sggName;
        this.emdName = emdName;
    }

    public static AddressResponse of(EmdAddress emdAddress) {
        return AddressResponse.builder()
                .emdId(emdAddress.getId())
                .sidoName(emdAddress.getSggAddress().getSidoAddress().getSidoName())
                .sggName(emdAddress.getSggAddress().getSggName())
                .emdName(emdAddress.getEmdName())
                .build();
    }
}
