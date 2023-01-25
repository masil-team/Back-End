package com.masil.domain.postlike.controller;

import com.masil.domain.post.dto.PostDetailResponse;
import com.masil.domain.postlike.dto.PostLikeResponse;
import com.masil.domain.postlike.service.PostLikeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Slf4j
public class PostLikeController {

    private final PostLikeService postLikeService;

    @PutMapping("/posts/{postId}/like")
    public ResponseEntity<PostLikeResponse> likePost(@PathVariable Long postId) {
        log.info("게시글 좋아요 시작");

        PostLikeResponse postLikeResponse = postLikeService.toggleLikePost(postId, 1L); // 추후 변경
        return ResponseEntity.ok(postLikeResponse);
    }
}
