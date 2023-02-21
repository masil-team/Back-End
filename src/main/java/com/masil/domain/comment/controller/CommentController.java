package com.masil.domain.comment.controller;

import com.masil.domain.comment.dto.*;
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
    public ResponseEntity<CommentsResponse> findComments(@PathVariable Long postId,
                                                         @PageableDefault(page = 0, size = 20) Pageable pageable,
                                                         @LoginUser CurrentMember currentMember){

        log.info("댓글 조회 시작");
        Long memberId = (currentMember != null) ? currentMember.getId() : null;
        CommentsResponse comments = commentService.findComments(postId, pageable, memberId);
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
     * 대댓글 작성
     */
    @PreAuthorize("isAuthenticated()")
    @PostMapping("/{postId}/reply/{commentId}")
    public ResponseEntity<ChildrenCreateRequest> createChildrenComment(@PathVariable Long postId,
                                                                       @PathVariable Long commentId,
                                                                       @Valid @RequestBody ChildrenCreateRequest childrenCreateRequest,
                                                                       @LoginUser CurrentMember currentMember){
        log.info("대댓글 생성 시작");
        commentService.createChildrenComment(postId, commentId, childrenCreateRequest, currentMember.getId());
        // 01-19 create로 수정
        return ResponseEntity.created(URI.create("/posts/" + postId + "/reply/" + commentId)).build();
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