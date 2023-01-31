package com.masil.domain.address.dto.request;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AddressSearchRequest {

    @NotBlank(message = "잘못된 검색어입니다.")
    private String searchWord;

    @Builder
    public AddressSearchRequest(String searchWord) {
        this.searchWord = searchWord;
    }
}
