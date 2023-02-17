package com.masil.domain.bookmark.dto;

import com.masil.domain.bookmark.entity.Bookmark;
import com.masil.domain.post.dto.PostsElementResponse;
import com.masil.domain.post.entity.Post;
import lombok.Getter;
import org.springframework.data.domain.Slice;

import java.util.List;
import java.util.stream.Collectors;

@Getter
public class BookmarksResponse {

    private List<BookmarksElementResponse> bookmarks;
    private Boolean isLast;

    public BookmarksResponse(List<BookmarksElementResponse> bookmarks, boolean isLast) {
        this.bookmarks = bookmarks;
        this.isLast = isLast;
    }
    public static BookmarksResponse ofBookmarks(Slice<Bookmark> bookmarks) {
        List<BookmarksElementResponse> postsResponse = bookmarks
                .stream()
                .map(bookmark -> BookmarksElementResponse.of(bookmark))
                .collect(Collectors.toList());

        return new BookmarksResponse(postsResponse, bookmarks.isLast());
    }

}
