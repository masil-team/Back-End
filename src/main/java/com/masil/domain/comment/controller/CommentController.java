package com.masil.domain.comment.controller;

import com.masil.domain.comment.dto.CommentCreateRequest;
import com.masil.domain.comment.dto.CommentModifyRequest;
import com.masil.domain.comment.dto.CommentResponse;
import com.masil.domain.comment.service.CommentService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/posts")
@Slf4j
public class CommentController {

    private final CommentService commentService;

    /**
     * 댓글 조회
     */
    @GetMapping("/{postId}/comments")
    public ResponseEntity<List<CommentResponse>> findComments(@PathVariable Long postId){
        log.info("댓글 조회 시작");
        List<CommentResponse> comments = commentService.findComments(postId);
        return ResponseEntity.status(HttpStatus.OK).body(comments);
    }

    /**
     * 댓글 작성
     */
    @PostMapping("/{postId}/comments")
    public ResponseEntity<CommentCreateRequest> createComment(@PathVariable Long postId,
                                                              @Valid @RequestBody CommentCreateRequest commentCreateRequest){
        log.info("댓글 생성 시작");
        commentService.createComment(/*String nickname,*/ postId, commentCreateRequest);
        // 추후에 created로 수정
        return ResponseEntity.ok().build();
    }

    /**
     * 댓글 수정
     */
    @PutMapping("/{postId}/comments/{commentId}")
    public ResponseEntity<CommentModifyRequest> modifyComment(@PathVariable Long postId, @Valid @RequestBody CommentModifyRequest commentModifyRequest,
                                                              @PathVariable Long commentId){
        log.info("댓글 수정 시작");
        commentService.modifyComment(postId, commentModifyRequest, commentId);
        return ResponseEntity.ok().build();
    }

    /**
     * 댓글 삭제
     */
    @DeleteMapping("/{postId}/comments/{commentId}")
    public ResponseEntity<Void> deleteComment(@PathVariable Long postId,
                                              @PathVariable Long commentId) {
        log.info("댓글 삭제 시작");
        commentService.deleteComment(postId, commentId);
        return ResponseEntity.noContent().build();
    }
}
