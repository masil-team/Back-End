package com.masil.domain.post.controller;

import com.masil.domain.post.dto.PostCreateRequest;
import com.masil.domain.post.dto.PostResponse;
import com.masil.domain.post.dto.PostsResponse;
import com.masil.domain.post.service.PostService;
import com.masil.domain.user.entity.User;
import com.masil.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/boards")
@Slf4j
public class PostController {

    private final PostService postService;
    private final UserRepository userRepository; // 추후 삭제

    // 단 건
    @GetMapping("/{boardId}/posts/{postId}")
    public ResponseEntity<PostResponse> findPost(@PathVariable Long boardId,
                                                 @PathVariable Long postId) {
        log.info("게시글 단건 조회 성공");
        PostResponse postResponse = postService.findPost(postId);
        return ResponseEntity.ok(postResponse);
    }

    // 다 건
    @GetMapping("/{boardId}/posts")
    public ResponseEntity<PostsResponse> findAllPost(@PathVariable Long boardId) {
        log.info("게시글 다건 조회 성공");
        PostsResponse postsResponse = postService.findAllPost();
        return ResponseEntity.ok(postsResponse);
    }

    // 생성
    @PostMapping("/{boardId}/posts")
    public ResponseEntity<Void> createPost(@Valid @RequestBody PostCreateRequest postCreateRequest) {
        log.info("게시글 생성 성공");
        User user = userRepository.findById(1L).get(); // 추후 삭제
        postService.createPost(postCreateRequest, user);
        return ResponseEntity.ok().build();
    }
}
