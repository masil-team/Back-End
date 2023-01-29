package com.masil.domain.commentlike.controller;

import com.masil.domain.commentlike.service.CommentLikeService;
import com.masil.global.auth.annotaion.LoginUser;
import com.masil.global.auth.dto.response.CurrentMember;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
public class CommentLikeController {
    private final CommentLikeService commentLikeService;

    @PutMapping("/comments/{commentId}/addLike")
    public ResponseEntity likeComment(@PathVariable Long commentId,
                                      @LoginUser CurrentMember currentMember) {
        commentLikeService.updateLikeOfComment(commentId, currentMember.getId());
        return ResponseEntity.ok().build();
    }
}


