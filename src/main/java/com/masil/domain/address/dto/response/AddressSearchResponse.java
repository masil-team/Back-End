package com.masil.domain.address.dto.response;

import com.masil.domain.address.entity.EmdAddress;
import com.masil.domain.address.entity.SggAddress;
import lombok.Builder;
import lombok.Getter;

@Getter
public class AddressSearchResponse {
    // TODO: 2023/02/03 시군구 리턴값일 때 emd데이터 null으로 리턴하는데 아예 없애고 리턴하기 -> 클래스 분리 필요

    private Integer rCode;
    private String rName;
    private Integer emdId;
    private Integer sggId;
    private Integer sidoId;
    private String sidoName;
    private String sggName;
    private String emdName;
    private boolean isEmdAddress;

    @Builder
    public AddressSearchResponse(Integer rCode, String rName, Integer emdId, Integer sggId, Integer sidoId, String sidoName, String sggName, String emdName, boolean isEmdAddress) {
        this.rCode = rCode;
        this.rName = rName;
        this.emdId = emdId;
        this.sggId = sggId;
        this.sidoId = sidoId;
        this.sidoName = sidoName;
        this.sggName = sggName;
        this.emdName = emdName;
        this.isEmdAddress = isEmdAddress;
    }

    public static AddressSearchResponse of(SggAddress sggAddress) {
        return AddressSearchResponse.builder()
                .rCode(sggAddress.getId())
                .rName(sggAddress.getSidoAddress().getSidoName() + " " + sggAddress.getSggName())
                .sggId(sggAddress.getId())
                .sggName(sggAddress.getSggName())
                .sidoId(sggAddress.getSidoAddress().getId())
                .sidoName(sggAddress.getSidoAddress().getSidoName())
                .isEmdAddress(false)
                .build();
    }

    public static AddressSearchResponse of(EmdAddress emdAddress) {
        return AddressSearchResponse.builder()
                .rCode(emdAddress.getId())
                .rName(emdAddress.getSggAddress().getSidoAddress().getSidoName() + " "
                        + emdAddress.getSggAddress().getSggName() + " " + emdAddress.getEmdName())
                .emdId(emdAddress.getId())
                .emdName(emdAddress.getEmdName())
                .sggId(emdAddress.getSggAddress().getId())
                .sggName(emdAddress.getSggAddress().getSggName())
                .sidoId(emdAddress.getSggAddress().getSidoAddress().getId())
                .sidoName(emdAddress.getSggAddress().getSidoAddress().getSidoName())
                .isEmdAddress(true)
                .build();
    }

    @Override
    public String toString() {
        return "AddressSearchResponse{" +
                "rCode=" + rCode +
                ", rName='" + rName + '\'' +
                ", emdId=" + emdId +
                ", sggId=" + sggId +
                ", sidoId=" + sidoId +
                ", sidoName='" + sidoName + '\'' +
                ", sggName='" + sggName + '\'' +
                ", emdName='" + emdName + '\'' +
                ", isEmdAddress=" + isEmdAddress +
                '}';
    }
}
