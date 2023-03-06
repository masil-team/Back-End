package com.masil.domain.storage.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class FileInfoDto {

    private String s3Name;
    private String url;


    public static FileInfoDto of(String s3Name, String url) {
        return FileInfoDto.builder()
                .s3Name(s3Name)
                .url(url)
                .build();
    }
}
