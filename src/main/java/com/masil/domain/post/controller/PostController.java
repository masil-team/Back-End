package com.masil.domain.post.controller;

import com.masil.domain.post.dto.PostResponse;
import com.masil.domain.post.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/boards")
public class PostController {

    private final PostService postService;

    // 단 건
    @GetMapping("/{boardId}/posts")
    public ResponseEntity<PostResponse> findPost(@PathVariable Long id) {
        PostResponse postResponse = postService.findPost(id);
        return ResponseEntity.ok(postResponse);
    }

    // 다 건
//    @GetMapping("/{boardId}/posts/{postId}")
//    public ResponseEntity<> findAllPost() {
//        postService.findAllPost();
//        return ResponseEntity.ok();
//    }


}
