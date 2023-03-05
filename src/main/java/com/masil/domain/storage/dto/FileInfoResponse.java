package com.masil.domain.storage.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class FileInfoResponse {

    private Long id;
    private String url;

    public static FileInfoResponse of(Long id, String url) {
        return FileInfoResponse.builder()
                .id(id)
                .url(url)
                .build();
    }
}
