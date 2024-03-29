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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class PostLikeController {

    private final PostLikeService postLikeService;

    @PreAuthorize("isAuthenticated()")
    @PutMapping("/posts/{postId}/modify-like")
    public ResponseEntity<PostLikeResponse> likePost(@PathVariable Long postId,
                                                     @LoginUser CurrentMember currentMember) {
        PostLikeResponse postLikeResponse = postLikeService.toggleLikePost(postId, currentMember.getId());
        return ResponseEntity.ok(postLikeResponse);
    }
}
