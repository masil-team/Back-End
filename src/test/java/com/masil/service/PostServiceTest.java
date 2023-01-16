package com.masil.service;

import com.masil.common.DatabaseCleaner;
import com.masil.domain.post.dto.PostCreateRequest;
import com.masil.domain.post.dto.PostModifyRequest;
import com.masil.domain.post.dto.PostResponse;
import com.masil.domain.post.dto.PostsResponse;
import com.masil.domain.post.entity.Post;
import com.masil.domain.post.entity.State;
import com.masil.domain.post.repository.PostRepository;

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

    @Autowired
    public PostRepository postRepository;

    @Autowired
    public DatabaseCleaner databaseCleaner;

    @BeforeEach
    void beforeEach() {
        databaseCleaner.execute();
        // 초기화
        User user = User.builder()
                .email("test@123")
                .build();
        userRepository.save(user);

        Post post1 = Post.builder()
                .content("내용1")
                .user(user)
                .build();

        Post post2 = Post.builder()
                .content("내용")
                .user(user)
                .build();

        postRepository.save(post1);
        postRepository.save(post2);
    }

    @DisplayName("단 건 게시글을 성공적으로 조회된다.")
    @Test
    void findPost_test() {

        // when
        PostResponse post = postService.findPost(1L);

        // then
        assertThat(post.getContent()).isEqualTo("내용1");
    }

    @DisplayName("다 건 게시글을 성공적으로 조회된다.")
    @Test
    void findAllPost_test() {

        // when
        PostsResponse allPost = postService.findAllPost();

        // then
        assertThat(allPost.getPosts().size()).isEqualTo(2);
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

    @DisplayName("게시글 수정이 성공")
    @Test
    void modifyPost_test() {

        // given
        String content = "수정 후 내용";
        Post beforePost = postRepository.findById(1L).get(); // "내용1"

        User user = userRepository.findById(1L).get();
        PostModifyRequest postModifyRequest = new PostModifyRequest(content);

        // when
        postService.modifyPost(beforePost.getId(), postModifyRequest, user.getId());

        // then
        Post afterPost = postRepository.findById(1L).get(); // "수정 후 내용"
        assertThat(afterPost.getContent()).isEqualTo(content);
    }

    @DisplayName("게시글 삭제 성공")
    @Test
    void deletePost_test() {

        // given
        Post beforePost = postRepository.findById(1L).get();  // status : NORMAL
        User user = userRepository.findById(1L).get();

        // when
        postService.deletePost(beforePost.getId(), user.getId());

        // then
        Post afterPost = postRepository.findById(1L).get(); // status : DELETE
        assertThat(afterPost.getState()).isEqualTo(State.DELETE);

    }
}
