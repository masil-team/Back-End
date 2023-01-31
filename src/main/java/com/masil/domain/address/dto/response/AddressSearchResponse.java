package com.masil.domain.address.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
public class AddressSearchResponse {

    private String emdId;
    private String sggId;
    private String sidoId;
    private String sidoName;
    private String sggName;
    private String emdName;

    @Builder
    public AddressSearchResponse(String emdId, String sggId, String sidoId, String sidoName, String sggName, String emdName) {
        this.emdId = emdId;
        this.sggId = sggId;
        this.sidoId = sidoId;
        this.sidoName = sidoName;
        this.sggName = sggName;
        this.emdName = emdName;
    }
}
