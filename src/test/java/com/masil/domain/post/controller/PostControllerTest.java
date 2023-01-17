package com.masil.domain.post.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.masil.domain.post.dto.*;
import com.masil.domain.post.service.PostService;
import com.masil.domain.user.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.hamcrest.Matchers.containsString;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest({
        PostController.class,
})
@AutoConfigureMockMvc(addFilters = false)
@AutoConfigureRestDocs
@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
public class PostControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    private PostService postService;

    private static final PostResponse POST_RESPONSE_1 = PostResponse.builder()
            .id(1L)
            .nickname("닉네임1")
            .content("내용1")
            .viewCount(0)
            .commentsResponse(null)
            .build();
    private static final PostResponse POST_RESPONSE_2 = PostResponse.builder()
            .id(2L)
            .nickname("닉네임2")
            .content("내용2")
            .viewCount(0)
            .commentsResponse(null)
            .build();

//    private static final String AUTHORIZATION_HEADER_NAME = "Authorization";
//    private static final String AUTHORIZATION_HEADER_VALUE = "Bearer aaaaaaaa.bbbbbbbb.cccccccc";

    @Test
    @DisplayName("게시글 생성을 성공한다.")
    void createPost_success() throws Exception {
        // given
        PostCreateRequest postCreateRequest = PostCreateRequestBuilder.build();

        given(postService.createPost(any(), any())).willReturn(1L);

        // when
        ResultActions resultActions = requestCreatePost(postCreateRequest);

        // then
        resultActions
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/boards/1/posts/1"))
                .andDo(document("post/create",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestFields(
                                fieldWithPath("content").description("내용")
                        )
                ));
    }

    @Test
    @DisplayName("게시글 단 건 조회를 성공한다.")
    void findPost_success() throws Exception {

        // given
        given(postService.findPost(any())).willReturn(POST_RESPONSE_1);

        // when
        ResultActions resultActions = requestFindPost();

        // then
        resultActions
                .andExpect(status().is2xxSuccessful())
                .andDo(document("post/findOne",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        responseFields(
                                fieldWithPath("id").description("게시글 id"),
                                fieldWithPath("nickname").description("닉네임"),
                                fieldWithPath("content").description("내용"),
                                fieldWithPath("viewCount").description(0),
                                fieldWithPath("comments").description("댓글")
                        )
                ));
    }

    @Test
    @DisplayName("게시글 다 건 조회를 성공한다.")
    void t3() throws Exception {
        // given
        List<PostResponse> postResponse = new ArrayList<>();
        postResponse.add(POST_RESPONSE_1);
        postResponse.add(POST_RESPONSE_2);
        PostsResponse postsResponse = new PostsResponse(postResponse);

        given(postService.findAllPost()).willReturn(postsResponse);

        // when
        ResultActions resultActions = requestFindAllPost();

        // then
        resultActions
                .andExpect(status().is2xxSuccessful())
                .andDo(document("post/findAll",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        responseFields(
                                fieldWithPath("posts.[].id").description("게시글 id 1"),
                                fieldWithPath("posts.[].nickname").description("닉네임"),
                                fieldWithPath("posts.[].content").description("내용1"),
                                fieldWithPath("posts.[].viewCount").description(0),
                                fieldWithPath("posts.[].comments").description("댓글"),
                                fieldWithPath("posts.[].id").description("게시글 id 2"),
                                fieldWithPath("posts.[].nickname").description("닉네임"),
                                fieldWithPath("posts.[].content").description("내용2"),
                                fieldWithPath("posts.[].viewCount").description(0),
                                fieldWithPath("posts.[].comments").description("댓글")
                        )
                ));
    }

    @Test
    @DisplayName("게시글 수정")
    void t4() throws Exception {
        // given
        PostModifyRequest postModifyRequest = PostModifyRequestBuilder.build();

        willDoNothing().given(postService).modifyPost(any(),any(),any());

        // when
        ResultActions resultActions = requestModifyPost(postModifyRequest);

        // then
        resultActions
                .andExpect(status().is2xxSuccessful())
                .andDo(document("post/modify",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestFields(
                                fieldWithPath("content").description("수정할 내용")
                        )
                ));
    }

    @Test
    @DisplayName("게시글 삭제")
    void t5() throws Exception {

        // given
        willDoNothing().given(postService).deletePost(any(), any());
        // when
        ResultActions resultActions = requestDeletePost();

        // then
        resultActions
                .andExpect(status().isNoContent())
                .andDo(document("post/delete",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint())
        ));
    }

    private ResultActions requestCreatePost(PostCreateRequest dto) throws Exception {
        return mockMvc.perform(post("/boards/1/posts")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andDo(print());
    }

    private ResultActions requestFindPost() throws Exception {
        return mockMvc.perform(get("/boards/1/posts/1")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print());
    }

    private ResultActions requestFindAllPost() throws Exception {
        return mockMvc.perform(get("/boards/1/posts/")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print());
    }
    private ResultActions requestModifyPost(PostModifyRequest dto) throws Exception {
        return mockMvc.perform(patch("/boards/1/posts/1")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andDo(print());
    }
    private ResultActions requestDeletePost() throws Exception {
        return mockMvc.perform(delete("/boards/1/posts/1")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print());
    }

}
