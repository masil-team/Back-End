package com.masil.domain.bookmark.controller;

import com.masil.domain.bookmark.dto.BookmarkResponse;
import com.masil.domain.bookmark.dto.BookmarksResponse;
import com.masil.domain.bookmark.service.BookmarkService;
import com.masil.domain.post.dto.PostDetailResponse;
import com.masil.global.auth.annotaion.LoginUser;
import com.masil.global.auth.dto.response.CurrentMember;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import static org.springframework.data.domain.Sort.Direction.DESC;

@Slf4j
@RestController
@RequiredArgsConstructor
public class BookmarkController {

    private final BookmarkService bookmarkService;

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/posts/{postId}/bookmark")
    public ResponseEntity<BookmarkResponse> addBookmark(@PathVariable Long postId,
                                                           @LoginUser CurrentMember currentMember) {
        log.info("즐겨찾기 추가 시작");

        BookmarkResponse bookmarkResponse = bookmarkService.addBookmark(postId, currentMember.getId());
        return ResponseEntity.ok(bookmarkResponse);
    }

    @PreAuthorize("isAuthenticated()")
    @DeleteMapping("/posts/{postId}/bookmark")
    public ResponseEntity<BookmarkResponse> deleteBookmark(@PathVariable Long postId,
                                                        @LoginUser CurrentMember currentMember) {
        log.info("즐겨찾기 삭제 시작");

        BookmarkResponse bookmarkResponse = bookmarkService.deleteBookmark(postId, currentMember.getId());
        return ResponseEntity.ok(bookmarkResponse);
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/bookmarks")
    public ResponseEntity<BookmarksResponse> findBookmarks(@LoginUser CurrentMember currentMember,
                                                           @PageableDefault(sort = "createDate", direction = DESC) Pageable pageable) {
        log.info("즐겨찾기 목록 조회 시작");

        BookmarksResponse bookmarksResponse = bookmarkService.findBookmarks(currentMember.getId(), pageable);
        return ResponseEntity.ok(bookmarksResponse);
    }
}
