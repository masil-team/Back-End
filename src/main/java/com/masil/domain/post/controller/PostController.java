package com.masil.domain.post.controller;

import com.masil.domain.post.dto.PostCreateRequest;

import com.masil.domain.post.dto.PostModifyRequest;

import com.masil.domain.post.dto.PostResponse;
import com.masil.domain.post.dto.PostsResponse;
import com.masil.domain.post.service.PostService;
import com.masil.domain.member.entity.Member;
import com.masil.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.security.Principal;

@RestController
@RequiredArgsConstructor
@RequestMapping("/boards")
@Slf4j
public class PostController {

    private final PostService postService;

    // 단 건
    @GetMapping("/{boardId}/posts/{postId}")
    public ResponseEntity<PostResponse> findPost(@PathVariable Long boardId,
                                                 @PathVariable Long postId) {
        log.info("게시글 단건 조회 시작");

        PostResponse postResponse = postService.findPost(postId);
        return ResponseEntity.ok(postResponse);
    }

    // 다 건
    @GetMapping("/{boardId}/posts")
    public ResponseEntity<PostsResponse> findAllPost(@PathVariable Long boardId) {
        log.info("게시글 다건 조회 시작");

        PostsResponse postsResponse = postService.findAllPost();
        return ResponseEntity.ok(postsResponse);
    }

    // 생성
    @PostMapping("/{boardId}/posts")
    public ResponseEntity<Void> createPost(@PathVariable Long boardId,
                                           @Valid @RequestBody PostCreateRequest postCreateRequest) {
        log.info("게시글 생성 시작");

        Long postId = postService.createPost(postCreateRequest, 1L); // 추후 변경
        return ResponseEntity.created(URI.create("/boards/" + boardId + "/posts/" + postId)).build();
    }

    // 수정
    @PatchMapping("/{boardId}/posts/{postId}")
    public ResponseEntity<Void> modifyPost(@PathVariable Long boardId,
                                           @PathVariable Long postId,
                                           @Valid @RequestBody PostModifyRequest postModifyRequest) {
        log.info("게시글 수정 시작");

        postService.modifyPost(postId, postModifyRequest, 1L);
        return ResponseEntity.ok().build();
    }

    // 삭제
    @DeleteMapping("/{boardId}/posts/{postId}")
    public ResponseEntity<Void> deletePost(@PathVariable Long boardId,
                                           @PathVariable Long postId) {
        log.info("게시글 삭제 시작");
        postService.deletePost(postId, 1L);
        return ResponseEntity.noContent().build();
    }

}
