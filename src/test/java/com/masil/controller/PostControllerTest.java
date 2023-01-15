package com.masil.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.masil.domain.post.controller.PostController;
import com.masil.domain.post.dto.PostCreateRequest;
import com.masil.domain.post.service.PostService;
import com.masil.domain.user.entity.User;
import com.masil.domain.user.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@ActiveProfiles("test")
public class PostControllerTest {

    @Autowired
    private MockMvc mvc;

    ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private PostService postService;
    @Autowired
    private UserRepository userRepository;

    @Test
    @DisplayName("게시글 생성")
    void t1() throws Exception {
        // given
        PostCreateRequest postCreateRequest = new PostCreateRequest( "Hello");

        // when, then
        ResultActions resultActions = mvc.perform(
                        post("/boards/1/posts")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(postCreateRequest)));

        resultActions
                /**
                 * isCreated() -> 상태코드 201
                 * controller에서 created() 201로 받아야 하는 이유 찾기
                 */
                .andExpect(status().isOk())
                .andExpect(handler().handlerType(PostController.class))
                .andExpect(handler().methodName("createPost"));

    }
    @Test
    @DisplayName("게시글 단 건 조회")
    void t2() throws Exception {
        // given
        User user = userRepository.findById(1L).get();
        PostCreateRequest postCreateRequest = new PostCreateRequest("content");
        Long postId = postService.createPost(postCreateRequest, user);

        String url = "/boards/1/posts/" + postId;
        // WHEN
        ResultActions resultActions = mvc
                .perform(get(url))
                .andDo(print());

        // THEN
        resultActions
                .andExpect(status().is2xxSuccessful())
                .andExpect(handler().handlerType(PostController.class))
                .andExpect(content().string(containsString("content")));
    }

    @Test
    @DisplayName("게시글 다 건 조회")
    void t3() throws Exception {
        // given

        String url = "/boards/1/posts/";
        // WHEN
        ResultActions resultActions = mvc
                .perform(get(url))
                .andDo(print());

        // THEN
        resultActions
                .andExpect(status().is2xxSuccessful())
                .andExpect(handler().handlerType(PostController.class));
    }
}
