package com.masil.domain.comment.controller;

import com.masil.domain.comment.dto.CommentCreateRequest;
import com.masil.domain.comment.dto.CommentModifyRequest;
import com.masil.domain.comment.dto.CommentResponse;
import com.masil.domain.comment.service.CommentService;
import com.masil.global.auth.annotaion.LoginUser;
import com.masil.global.auth.dto.response.CurrentMember;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

import static org.springframework.data.domain.Sort.Direction.DESC;

@RestController
@RequiredArgsConstructor
@RequestMapping("/posts")
@Slf4j
public class CommentController {

    private final CommentService commentService;

    /**
     * 댓글 조회
     * 페이징 처리
     */
    @GetMapping("/{postId}/comments")
    public ResponseEntity<List<CommentResponse>> findComments(@PathVariable Long postId,
                                                              @PageableDefault(page = 0, size = 5, direction = DESC) Pageable pageable){

        log.info("댓글 조회 시작");

        List<CommentResponse> comments = commentService.findComments(postId, pageable);
        return ResponseEntity.status(HttpStatus.OK).body(comments);
    }

    /**
     * 댓글 작성
     */
    @PreAuthorize("isAuthenticated()")
    @PostMapping("/{postId}/comments")
    public ResponseEntity<CommentCreateRequest> createComment(@PathVariable Long postId,
                                                              @Valid @RequestBody CommentCreateRequest commentCreateRequest,
                                                              @LoginUser CurrentMember currentMember){
        log.info("댓글 생성 시작");
        commentService.createComment(postId, commentCreateRequest, currentMember.getId());
        // 01-19 create로 수정
        return ResponseEntity.created(URI.create("/posts/" + postId + "/comments")).build();
    }

    /**
     * 댓글 수정
     */
    @PreAuthorize("isAuthenticated()")
    @PatchMapping("/{postId}/comments/{commentId}")
    public ResponseEntity<CommentModifyRequest> modifyComment(@PathVariable Long postId, @Valid @RequestBody CommentModifyRequest commentModifyRequest,
                                                              @PathVariable Long commentId,
                                                              @LoginUser CurrentMember currentMember){
        log.info("댓글 수정 시작");
        commentService.modifyComment(postId, commentModifyRequest, commentId, currentMember.getId());
        return ResponseEntity.ok().build();
    }

    /**
     * 댓글 삭제
     */
    @PreAuthorize("isAuthenticated()")
    @DeleteMapping("/{postId}/comments/{commentId}")
    public ResponseEntity<Void> deleteComment(@PathVariable Long postId,
                                              @PathVariable Long commentId,
                                              @LoginUser CurrentMember currentMember) {
        log.info("댓글 삭제 시작");
        commentService.deleteComment(postId, commentId, currentMember.getId());
        return ResponseEntity.noContent().build();
    }
}