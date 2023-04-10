package com.masil.domain.commentlike.controller;

import com.masil.domain.commentlike.dto.CommentLikeResponse;
import com.masil.domain.commentlike.service.CommentLikeService;
import com.masil.global.auth.annotaion.LoginUser;
import com.masil.global.auth.dto.response.CurrentMember;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class CommentLikeController {
    private final CommentLikeService commentLikeService;

    @PutMapping("/comments/{commentId}/modify-like")
    public ResponseEntity<CommentLikeResponse> likeComment(@PathVariable Long commentId,
                                                           @LoginUser CurrentMember currentMember) {
        CommentLikeResponse commentLikeResponse = commentLikeService.updateLikeOfComment(commentId, currentMember.getId());
        return ResponseEntity.ok(commentLikeResponse);
    }
}


