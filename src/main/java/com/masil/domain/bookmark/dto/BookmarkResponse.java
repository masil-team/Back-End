package com.masil.domain.bookmark.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class BookmarkResponse {
    private Boolean isScrap;

    public static BookmarkResponse of(Boolean isScrap) {
        return new BookmarkResponse(isScrap);
    }
}
