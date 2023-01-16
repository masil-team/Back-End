package com.masil.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.masil.domain.comment.dto.CommentResponse;
import com.masil.domain.post.controller.PostController;
import com.masil.domain.post.dto.PostCreateRequest;

import com.masil.domain.post.dto.PostModifyRequest;
import com.masil.domain.post.entity.Post;
import com.masil.domain.post.entity.State;
import com.masil.domain.post.repository.PostRepository;
import com.masil.domain.post.service.PostService;
import com.masil.domain.user.entity.User;
import com.masil.domain.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.containsString;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


//@WebMvcTest({
//        PostService.class,
//        UserRepository.class,
//})
@ExtendWith(RestDocumentationExtension.class)
@AutoConfigureRestDocs
@AutoConfigureMockMvc
@Transactional
@ActiveProfiles("test")
//@ExtendWith(MockitoExtension.class)
@SpringBootTest
public class PostControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    public PostRepository postRepository;

    @Autowired
    private PostService postService;
    @Autowired
    private UserRepository userRepository;
//    @BeforeEach
//    void setUp(WebApplicationContext webApplicationContext, RestDocumentationContextProvider restDocumentation) {
//        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
//                .apply(documentationConfiguration(restDocumentation))
//                .build();
//    }
    @BeforeEach
    void beforeEach() {
        // 초기화
        User user = User.builder()
                .email("test@123")
                .nickname("test")
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

//    @Test
//    @DisplayName("게시글 생성")
//    void t1() throws Exception {
//        // given
//        PostCreateRequest postCreateRequest = new PostCreateRequest( "Hello");
//
//        // when, then
//        ResultActions resultActions = mvc.perform(
//                        post("/boards/1/posts")
//                                .accept(MediaType.APPLICATION_JSON)
//                                .contentType(MediaType.APPLICATION_JSON)
//                                .content(objectMapper.writeValueAsString(postCreateRequest)));
//
//        resultActions
//                /**
//                 * isCreated() -> 상태코드 201
//                 * controller에서 created() 201로 받아야 하는 이유 찾기
//                 */
//                .andExpect(status().isOk())
//                .andExpect(handler().handlerType(PostController.class))
//                .andExpect(handler().methodName("createPost"));
//    }


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
                .andExpect(content().string(containsString("content")))
                .andDo(document("post/findOne",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        responseFields(
                                fieldWithPath("id").type(JsonFieldType.NUMBER).description("게시글 id"),
                                fieldWithPath("nickname").type(JsonFieldType.STRING).description("닉네임"),
                                fieldWithPath("content").type(JsonFieldType.STRING).description("내용"),
                                fieldWithPath("viewCount").type(JsonFieldType.NUMBER).description(0),
                                fieldWithPath("comments").type(JsonFieldType.NULL).description("댓글")
                        )
                ));
    }

//    @Test
//    @DisplayName("게시글 다 건 조회")
//    void t3() throws Exception {
//        // given
//
//        String url = "/boards/1/posts/";
//        // WHEN
//        ResultActions resultActions = mvc
//                .perform(get(url))
//                .andDo(print());
//
//        // THEN
//        resultActions
//                .andExpect(status().is2xxSuccessful())
//                .andExpect(handler().handlerType(PostController.class));
//    }
//
//    @Test
//    @DisplayName("게시글 수정")
//    void t4() throws Exception {
//        // given
//        PostModifyRequest postModifyRequest = new PostModifyRequest( "수정된 내용");
//
//        // WHEN
//        ResultActions resultActions = mvc
//                .perform(get("/boards/{boardId}/posts/{postId}",1,1)
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(objectMapper.writeValueAsString(postModifyRequest)))
//                .andDo(print());
//
//        // THEN
//        resultActions
//                .andExpect(status().is2xxSuccessful());
//    }
//
//    @Test
//    @DisplayName("게시글 삭제")
//    void t5() throws Exception {
//
//        // WHEN
//        ResultActions resultActions = mvc
//                .perform(get("/boards/{boardId}/posts/{postId}",1,1)
//                        .contentType(MediaType.APPLICATION_JSON))
//                .andDo(print());
//
//        // THEN
//        resultActions
//                .andExpect(status().is2xxSuccessful());
//    }

}
