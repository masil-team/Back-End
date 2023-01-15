package com.masil.service;

import com.masil.domain.post.dto.PostCreateRequest;
import com.masil.domain.post.dto.PostResponse;
import com.masil.domain.post.service.PostService;
import com.masil.domain.user.entity.User;
import com.masil.domain.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
@SpringBootTest
@Transactional
public class PostServiceTest {

    @Autowired
    public PostService postService;
    @Autowired
    public UserRepository userRepository;

    @BeforeEach
    void beforeEach() {
        // 초기화
    }

    @DisplayName("단 건 게시글을 성공적으로 조회된다.")
    @Test
    void findPost_test() {
        // given
        String content = "안녕하세요";
        User user = userRepository.findById(1L).get();
        PostCreateRequest postCreateRequest = new PostCreateRequest(content);
        Long postId = postService.createPost(postCreateRequest, user);

        // when
        PostResponse post = postService.findPost(postId);

        // then
        assertThat(postId).isEqualTo(post.getId());
    }

    @DisplayName("게시글 성공적으로 생성된다.")
    @Test
    void createPost_test() {
        // given
        String content = "안녕하세요";
        User user = userRepository.findById(1L).get();
        PostCreateRequest postCreateRequest = new PostCreateRequest(content);
        // when
        Long postId = postService.createPost(postCreateRequest, user);
        PostResponse post = postService.findPost(postId);


        // then
        assertThat(postId).isEqualTo(post.getId());
    }

}
