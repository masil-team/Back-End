package com.masil.domain.postlike.controller;

import com.masil.domain.postlike.dto.PostLikeResponse;
import com.masil.domain.postlike.service.PostLikeService;
import com.masil.global.auth.annotaion.LoginUser;
import com.masil.global.auth.dto.response.CurrentMember;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
public class PostLikeController {

    private final PostLikeService postLikeService;

    @PreAuthorize("isAuthenticated()")
    @PutMapping("/posts/{postId}/like")
    public ResponseEntity<PostLikeResponse> likePost(@PathVariable Long postId,
                                                     @LoginUser CurrentMember currentMember) {
        log.info("게시글 좋아요 시작");

        PostLikeResponse postLikeResponse = postLikeService.toggleLikePost(postId, currentMember.getId());
        return ResponseEntity.ok(postLikeResponse);
    }
}
