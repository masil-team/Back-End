package com.masil.domain.bookmark.controller;

import com.masil.domain.bookmark.dto.BookmarkResponse;
import com.masil.domain.bookmark.service.BookmarkService;
import com.masil.global.auth.annotaion.LoginUser;
import com.masil.global.auth.dto.response.CurrentMember;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class BookmarkController {

    private final BookmarkService bookmarkService;

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/posts/{postId}/bookmark")
    public ResponseEntity<BookmarkResponse> addBookmark(@PathVariable Long postId,
                                                           @LoginUser CurrentMember currentMember) {
        BookmarkResponse bookmarkResponse = bookmarkService.addBookmark(postId, currentMember.getId());
        return ResponseEntity.ok(bookmarkResponse);
    }

    @PreAuthorize("isAuthenticated()")
    @DeleteMapping("/posts/{postId}/bookmark")
    public ResponseEntity<BookmarkResponse> deleteBookmark(@PathVariable Long postId,
                                                        @LoginUser CurrentMember currentMember) {
        BookmarkResponse bookmarkResponse = bookmarkService.deleteBookmark(postId, currentMember.getId());
        return ResponseEntity.ok(bookmarkResponse);
    }
}
