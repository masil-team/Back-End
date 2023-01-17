package com.masil.domain.post.service;

import com.masil.common.DatabaseCleaner;
import com.masil.common.annotation.ServiceTest;
import com.masil.domain.post.dto.PostCreateRequest;
import com.masil.domain.post.dto.PostModifyRequest;
import com.masil.domain.post.dto.PostResponse;
import com.masil.domain.post.dto.PostsResponse;
import com.masil.domain.post.entity.Post;
import com.masil.domain.post.entity.State;
import com.masil.domain.post.repository.PostRepository;
import com.masil.domain.user.entity.User;
import com.masil.domain.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;

public class PostServiceTest extends ServiceTest {

    @Autowired
    public PostService postService;
    @Autowired
    public UserRepository userRepository;
    @Autowired
    public PostRepository postRepository;

    private static final String POST_CONTENT_1 = "내용1";
    private static final String POST_CONTENT_2 = "내용2";
    private static final String USER_EMAIL = "email@naver.com";
    private static final String USER_NICKNAME = "test123";

    @BeforeEach
    void setUp() {
        User user = User.builder()
                .email(USER_EMAIL)
                .nickname(USER_NICKNAME)
                .build();
        userRepository.save(user);

        Post post1 = Post.builder()
                .content(POST_CONTENT_1)
                .user(user)
                .build();
        Post post2 = Post.builder()
                .content(POST_CONTENT_2)
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
        assertThat(post.getId()).isEqualTo(1L);
        assertThat(post.getContent()).isEqualTo(POST_CONTENT_1);
        assertThat(post.getNickname()).isEqualTo(USER_NICKNAME);
        assertThat(post.getViewCount()).isEqualTo(0);
        assertThat(post.getComments()).isNull();
    }

    @DisplayName("다 건 게시글을 성공적으로 조회된다.")
    @Test
    void findAllPost_test() {

        // when
        PostsResponse allPost = postService.findAllPost();

        // then
        assertThat(allPost.getPosts().size()).isEqualTo(2);
    }

    @DisplayName("게시글이 성공적으로 생성된다.")
    @Test
    void createPost_test() {
        // given
        User user = userRepository.findById(1L).get();

        String content = "새로운 내용";
        PostCreateRequest postCreateRequest = new PostCreateRequest(content);

        // when
        Long postId = postService.createPost(postCreateRequest, 1L);
        PostResponse post = postService.findPost(postId);


        // then
        assertThat(post.getId()).isEqualTo(postId);
        assertThat(post.getContent()).isEqualTo(content);
        assertThat(post.getNickname()).isEqualTo(user.getNickname());
        assertThat(post.getViewCount()).isEqualTo(0);
    }

    @DisplayName("게시글 수정이 성공적으로 수행된다.")
    @Test
    void modifyPost_test() {

        // given
        User user = userRepository.findById(1L).get();

        String content = "수정 후 내용";
        Post beforePost = postRepository.findById(1L).get();

        PostModifyRequest postModifyRequest = new PostModifyRequest(content);

        // when
        postService.modifyPost(beforePost.getId(), postModifyRequest, user.getId());

        // then
        Post afterPost = postRepository.findById(1L).get();
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
