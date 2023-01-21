package com.masil.domain.post.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.masil.domain.member.dto.response.MemberResponse;
import com.masil.domain.post.dto.*;
import com.masil.domain.post.service.PostService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
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
@MockBean(JpaMetamodelMappingContext.class)
public class PostControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    private PostService postService;

    private static final MemberResponse MEMBER_RESPONSE = MemberResponse.builder()
            .id(1L)
            .nickname("닉네임1")
            .build();
    private static final PostDetailResponse POST_RESPONSE_1 = PostDetailResponse.builder()
            .id(1L)
            .member(MEMBER_RESPONSE)
            .content("내용1")
            .viewCount(0)
            .likeCount(0)
            .isOwner(false)
            .isLike(false)
            .createDate(LocalDateTime.now())
            .modifyDate(LocalDateTime.now())
            .build();

    private static final PostDetailResponse POST_RESPONSE_2 = PostDetailResponse.builder()
            .id(2L)
            .member(MEMBER_RESPONSE)
            .content("내용2")
            .viewCount(0)
            .likeCount(0)
            .isOwner(false)
            .isLike(false)
            .createDate(LocalDateTime.now())
            .modifyDate(LocalDateTime.now())
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
    @DisplayName("게시글 상세 조회를 성공한다.")
    void findPost_success() throws Exception {

        // given
        given(postService.findDetailPost(any(), any())).willReturn(POST_RESPONSE_1);

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
                                fieldWithPath("member.id").description("작성자 id"),
                                fieldWithPath("member.nickname").description("닉네임"),
                                fieldWithPath("content").description("내용"),
                                fieldWithPath("viewCount").description("조회수"),
                                fieldWithPath("likeCount").description("좋아요 개수"),
                                fieldWithPath("isOwner").description("본인 게시글 여부"),
                                fieldWithPath("isLike").description("게시글 좋아요 여부"),
                                fieldWithPath("createDate").description("생성 날짜"),
                                fieldWithPath("modifyDate").description("수정 날짜")
                        )
                ));
    }

    @Test
    @DisplayName("게시글 목록 조회를 성공한다.")
    void t3() throws Exception {
        // given
        List<PostsElementResponse> postsElementResponseList = new ArrayList<>();
        for (int i = 1; i <= 2; i++) {
            postsElementResponseList.add(PostsElementResponse.builder()
                    .id((long) i)
                    .member(MEMBER_RESPONSE)
                    .content("내용")
                    .viewCount(0)
                    .likeCount(0)
                    .commentCount(0)
                    .createDate(LocalDateTime.now())
                    .modifyDate(LocalDateTime.now())
                    .build());
        }

        PostsResponse postsResponse = new PostsResponse(postsElementResponseList);

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
                                fieldWithPath("posts.[].id").description("게시글 id"),
                                fieldWithPath("posts.[].member.id").description("작성자 id"),
                                fieldWithPath("posts.[].member.nickname").description("닉네임"),
                                fieldWithPath("posts.[].content").description("내용"),
                                fieldWithPath("posts.[].viewCount").description("조회수"),
                                fieldWithPath("posts.[].likeCount").description("좋아요 개수"),
                                fieldWithPath("posts.[].commentCount").description("댓글 개수"),
                                fieldWithPath("posts.[].createDate").description("생성 날짜"),
                                fieldWithPath("posts.[].modifyDate").description("수정 날짜"),
                                fieldWithPath("posts.[].id").description("게시글 id"),
                                fieldWithPath("posts.[].member.id").description("작성자 id"),
                                fieldWithPath("posts.[].member.nickname").description("닉네임"),
                                fieldWithPath("posts.[].content").description("내용"),
                                fieldWithPath("posts.[].viewCount").description("조회수"),
                                fieldWithPath("posts.[].likeCount").description("좋아요 개수"),
                                fieldWithPath("posts.[].commentCount").description("댓글 개수"),
                                fieldWithPath("posts.[].createDate").description("생성 날짜"),
                                fieldWithPath("posts.[].modifyDate").description("수정 날짜")
                        )
                ));
    }
//    private Long id;
//    private Long memberId;
//    private String nickname;
//    private String content;  // 글자 제한
//    private int viewCount;
//    private int likeCount;
//    private int commentCount;
//    private LocalDateTime createDate;
//    private LocalDateTime modifyDate;
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
