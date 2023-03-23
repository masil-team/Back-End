package com.masil.domain.post.controller;

import com.masil.domain.post.dto.*;
import com.masil.domain.post.service.PostService;
import com.masil.global.auth.annotaion.LoginUser;
import com.masil.global.auth.dto.response.CurrentMember;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Nullable;
import javax.validation.Valid;
import java.net.URI;

import static org.springframework.data.domain.Sort.Direction.DESC;

@RestController
@RequiredArgsConstructor
@RequestMapping()
@Slf4j
public class PostController {

    private final PostService postService;

    // 상세 조회
    @GetMapping("/posts/{postId}")
    public ResponseEntity<PostDetailResponse> findDetailPost(@PathVariable Long postId,
                                                             @LoginUser CurrentMember currentMember) {
        log.info("게시글 상세 조회 시작");

        Long memberId = (currentMember != null) ? currentMember.getId() : null;
        PostDetailResponse postDetailResponse = postService.findDetailPost(postId, memberId);
        return ResponseEntity.ok(postDetailResponse);
    }

    // 목록 조회
    @GetMapping("/boards/{boardId}/posts")
    public ResponseEntity<PostsResponse> findPosts(@PathVariable Long boardId,
                                                     @LoginUser CurrentMember currentMember,
                                                     @RequestParam("rCode") Integer rCode,
                                                     @PageableDefault(sort = "createDate", direction = DESC) Pageable pageable) {
        log.info("게시글 목록 조회 시작");

        Long memberId = (currentMember != null) ? currentMember.getId() : null;
        PostFilterRequest postFilterRequest = new PostFilterRequest(boardId, rCode, pageable);
        PostsResponse postsResponse = postService.findPosts(postFilterRequest, memberId);
        return ResponseEntity.ok(postsResponse);
    }

    // 생성
    @PreAuthorize("isAuthenticated()")
    @PostMapping("/posts")
    public ResponseEntity<Void> createPost(@Valid @RequestBody PostCreateRequest postCreateRequest,
                                           @LoginUser CurrentMember currentMember) {
        log.info("게시글 생성 시작");
        Long postId = postService.createPost(postCreateRequest, currentMember.getId());
        return ResponseEntity.created(URI.create("/posts/" + postId)).build();
    }

    // 수정
    @PreAuthorize("isAuthenticated()")
    @PatchMapping("/posts/{postId}")
    public ResponseEntity<Void> modifyPost(@PathVariable Long postId,
                                           @Valid @RequestBody PostModifyRequest postModifyRequest,
                                           @LoginUser CurrentMember currentMember) {
        log.info("게시글 수정 시작");

        postService.modifyPost(postId, postModifyRequest, currentMember.getId());
        return ResponseEntity.ok().build();
    }

    // 삭제
    @PreAuthorize("isAuthenticated()")
    @DeleteMapping("/posts/{postId}")
    public ResponseEntity<Void> deletePost(@PathVariable Long postId,
                                           @LoginUser CurrentMember currentMember) {
        log.info("게시글 삭제 시작");

        postService.deletePost(postId, currentMember.getId());
        return ResponseEntity.noContent().build();
    }

    /**
     * 게시글 검색
     */
    @GetMapping("/posts/search")
    public ResponseEntity<PostsResponse> searchPost(@RequestParam @Nullable String keyword,
                                                    @RequestParam("rCode") Integer rCode, // rCode 추가
                                                    @PageableDefault(sort = "createDate", direction = DESC, size = 8) Pageable pageable){
        log.info("게시글 검색 시작");
        PostsResponse postsResponse = postService.searchPosts(keyword, rCode, pageable);
        return ResponseEntity.ok(postsResponse);
    }



}
