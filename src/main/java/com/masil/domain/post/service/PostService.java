package com.masil.domain.post.service;

import com.masil.domain.post.dto.PostResponse;
import com.masil.domain.post.entity.Post;
import com.masil.domain.post.exception.PostNotFoundException;
import com.masil.domain.post.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;

    /**
     * 단건 조회 게시글 생성 작업 이후 구현 예정
     */

    public PostResponse findPost(Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(PostNotFoundException::new);

        return PostResponse.of(post);
    }
}
