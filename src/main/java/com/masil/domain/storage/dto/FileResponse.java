package com.masil.domain.storage.dto;

import com.masil.domain.postFile.entity.PostFile;
import lombok.Builder;
import lombok.Getter;


@Getter
@Builder
public class FileResponse {

    private String url;
    private int width;
    private int height;


    public static FileResponse of(PostFile postFile) {
        return FileResponse.builder()
                .url("https://dmpoeindaa9de.cloudfront.net" + postFile.getName()) // TODO : 추후 Util로 빼줄 예정
                .width(postFile.getWidth())
                .height(postFile.getHeight())
                .build();
    }
}