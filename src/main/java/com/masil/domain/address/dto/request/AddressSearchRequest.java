package com.masil.domain.address.dto.request;

import lombok.Builder;

import javax.validation.constraints.NotBlank;

public class AddressSearchRequest {

    @NotBlank(message = "잘못된 검색어 입니다.")
    private String searchWord;

    @Builder
    public AddressSearchRequest(String searchWord) {
        this.searchWord = searchWord;
    }
}
