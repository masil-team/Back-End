package com.masil.domain.comment.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.masil.common.security.WithMockCustomUser;
import com.masil.domain.comment.dto.*;
import com.masil.domain.comment.service.CommentService;
import com.masil.domain.member.dto.response.MemberResponse;
import com.masil.global.auth.jwt.provider.JwtTokenProvider;
import com.masil.global.config.security.SecurityConfig;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetailsService;
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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest({
        CommentController.class,
})
@AutoConfigureRestDocs
@ExtendWith(MockitoExtension.class)
@MockBean(JpaMetamodelMappingContext.class)
@Import(SecurityConfig.class)
@AutoConfigureMockMvc(addFilters = false)
@WithMockCustomUser // 유저 인증 정보
@ActiveProfiles("test")
class CommentControllerTest {

    @Autowired
    protected MockMvc mockMvc;
    @Autowired
    protected ObjectMapper objectMapper;

    @MockBean
    protected JwtTokenProvider jwtTokenProvider;

    @MockBean
    protected UserDetailsService userDetailsService;

    @MockBean
    private CommentService commentService;

    private static final MemberResponse MEMBER_RESPONSE = MemberResponse.builder()
            .id(1L)
            .nickname("닉네임1")
            .build();

    private static final String AUTHORIZATION_HEADER_VALUE = "Bearer aaaaaaaa.bbbbbbbb.cccccccc";

    @Test
    @DisplayName("댓글을 성공적으로 조회한다.")
    void findComments() throws Exception {
        //given
        List<CommentResponse> commentResponseList = new ArrayList<>();
        for (long i = 1; i <= 2 ; i++) {
            CommentResponse commentResponse = new CommentResponse(i, 1L, "내용A", "bw1111", 1L, 0, LocalDateTime.now(), LocalDateTime.now());
            commentResponseList.add(commentResponse);
        }

        given(commentService.findComments(any(), any())).willReturn(commentResponseList);

        //when
        mockMvc.perform(get("/posts/1/comments")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful())
                .andDo(document("comment/find",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        responseFields(
                                fieldWithPath("[].id").description("댓글 id"),
                                fieldWithPath("[].postId").description("게시글 id"),
                                fieldWithPath("[].content").description("댓글 내용"),
                                fieldWithPath("[].nickname").description("닉네임"),
                                fieldWithPath("[].memberId").description("멤버 id"),
                                fieldWithPath("[].likeCount").description("좋아요 갯수"),
                                fieldWithPath("[].createDate").description("생성 날짜"),
                                fieldWithPath("[].modifyDate").description("수정 날짜"),
                                fieldWithPath("[].id").description("댓글 id"),
                                fieldWithPath("[].postId").description("게시글 id"),
                                fieldWithPath("[].content").description("댓글 내용"),
                                fieldWithPath("[].nickname").description("닉네임"),
                                fieldWithPath("[].memberId").description("멤버 id"),
                                fieldWithPath("[].likeCount").description("좋아요 갯수"),
                                fieldWithPath("[].createDate").description("생성 날짜"),
                                fieldWithPath("[].modifyDate").description("수정 날짜")
                        )
                ));
    }

    @Test
    @DisplayName("댓글을 성공적으로 생성한다.")
    void createComment() throws Exception {
        // given
        CommentCreateRequest commentCreateRequest = CommentCreateRequestBuilder.build();

        given(commentService.createComment(any(), any(), any())).willReturn(1L);

        // when
        ResultActions resultActions = requestCreateComment(commentCreateRequest);

        // then
        resultActions
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/posts/1/comments"))
                .andDo(document("comment/create",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestFields(
                                fieldWithPath("content").description("내용")
                        )
                ));
    }

    @Test
    @DisplayName("댓글을 성공적으로 수정한다.")
    void modifyComment() throws Exception {
        // given
        CommentModifyRequest commentModifyRequest = CommentModifyRequestBuilder.build();

        willDoNothing().given(commentService).modifyComment(any(),any(),any(), any());

        // when
        ResultActions resultActions = requestModifyComment(commentModifyRequest);

        // then
        resultActions
                .andExpect(status().is2xxSuccessful())
                .andDo(document("comment/modify",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestFields(
                                fieldWithPath("content").description("수정할 댓글")
                        )
                ));
    }

    @Test
    @DisplayName("댓글을 성공적으로 삭제한다.")
    void deleteComment() throws Exception {
        // given
        willDoNothing().given(commentService).deleteComment(any(), any(), any());
        // when
        ResultActions resultActions = requestDeleteComment("/posts/1/comments/1");

        // then
        resultActions
                .andExpect(status().isNoContent())
                .andDo(document("comment/delete",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint())
                ));
    }

    private ResultActions requestCreateComment(CommentCreateRequest dto) throws Exception {
        return mockMvc.perform(post("/posts/1/comments")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, AUTHORIZATION_HEADER_VALUE)
                        .content(objectMapper.writeValueAsString(dto)))
                .andDo(print());
    }

    private ResultActions requestModifyComment(CommentModifyRequest dto) throws Exception {
        return mockMvc.perform(patch("/posts/1/comments/1")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, AUTHORIZATION_HEADER_VALUE)
                        .content(objectMapper.writeValueAsString(dto)))
                .andDo(print());
    }

    private ResultActions requestDeleteComment(String url) throws Exception {
        return mockMvc.perform(delete(url)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, AUTHORIZATION_HEADER_VALUE))
                .andDo(print());
    }
}