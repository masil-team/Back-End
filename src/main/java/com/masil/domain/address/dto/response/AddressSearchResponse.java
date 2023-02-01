package com.masil.domain.address.dto.response;

import com.masil.domain.address.entity.EmdAddress;
import com.masil.domain.address.entity.SggAddress;
import lombok.Builder;
import lombok.Getter;

@Getter
public class AddressSearchResponse {

    private Long emdId;
    private Long sggId;
    private Long sidoId;
    private String sidoName;
    private String sggName;
    private String emdName;

    @Builder
    public AddressSearchResponse(Long emdId, Long sggId, Long sidoId, String sidoName, String sggName, String emdName) {
        this.emdId = emdId;
        this.sggId = sggId;
        this.sidoId = sidoId;
        this.sidoName = sidoName;
        this.sggName = sggName;
        this.emdName = emdName;
    }

    public static AddressSearchResponse of(SggAddress sggAddress) {
        return AddressSearchResponse.builder()
                .sggId(sggAddress.getId())
                .sggName(sggAddress.getSggName())
                .sidoId(sggAddress.getSidoAddress().getId())
                .sidoName(sggAddress.getSidoAddress().getSidoName())
                .build();
    }

    public static AddressSearchResponse of(EmdAddress emdAddress) {
        return AddressSearchResponse.builder()
                .emdId(emdAddress.getId())
                .emdName(emdAddress.getEmdName())
                .sggId(emdAddress.getSggAddress().getId())
                .sggName(emdAddress.getSggAddress().getSggName())
                .sidoId(emdAddress.getSggAddress().getSidoAddress().getId())
                .sidoName(emdAddress.getSggAddress().getSidoAddress().getSidoName())
                .build();
    }
}
