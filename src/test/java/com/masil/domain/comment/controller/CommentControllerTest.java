package com.masil.domain.comment.controller;

import com.masil.common.annotation.ControllerMockApiTest;
import com.masil.domain.comment.dto.*;
import com.masil.domain.comment.exception.CommentAccessDeniedException;
import com.masil.domain.comment.exception.CommentInputException;
import com.masil.domain.comment.service.CommentService;
import com.masil.domain.member.dto.response.MemberResponse;
import com.masil.domain.post.dto.PostsResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.ResultActions;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.*;
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
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("test")
class CommentControllerTest extends ControllerMockApiTest{

    @MockBean
    private CommentService commentService;

    private static final MemberResponse MEMBER_RESPONSE1 = MemberResponse.builder()
            .id(1L)
            .nickname("닉네임1")
            .build();

    private static final MemberResponse MEMBER_RESPONSE2 = MemberResponse.builder()
            .id(1L)
            .nickname("닉네임2")
            .build();
    private static final ChildrenResponse REPLIES_RESPONSE_1 = ChildrenResponse.builder()
            .id(1L)
            .content("대댓글")
            .postId(1L)
            .member(MEMBER_RESPONSE2)
            .likeCount(0)
            .createDate(LocalDateTime.now())
            .modifyDate(LocalDateTime.now())
            .build();


    private static final String AUTHORIZATION_HEADER_VALUE = "Bearer aaaaaaaa.bbbbbbbb.cccccccc";

    @Test
    @DisplayName("댓글을 성공적으로 조회한다.")
    void findComments() throws Exception {
//        given
        List<ChildrenResponse> childrenResponseList = new ArrayList<>();
        childrenResponseList.add(REPLIES_RESPONSE_1);
        List<CommentResponse> commentResponses = new ArrayList<>();

        for (int i = 0; i < 1; i++) {
            commentResponses.add(CommentResponse.builder()
                    .id((long) i)
                    .postId(1L)
                    .content("댓글")
                    .member(MEMBER_RESPONSE1)
                    .likeCount(0)
                    .createDate(LocalDateTime.now())
                    .modifyDate(LocalDateTime.now())
                    .replies(childrenResponseList)
                    .build());
        }

        CommentsResponse commentResponseList = new CommentsResponse(commentResponses, 2L ,1);

        given(commentService.findComments(any(), any(), any())).willReturn(commentResponseList);


        //when
        mockMvc.perform(get("/posts/1/comments?page=0")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful())
                .andDo(document("comment/find",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        responseFields(
                                fieldWithPath("comments").description("댓글"),
                                fieldWithPath("comments.[].id").description("댓글 id"),
                                fieldWithPath("comments.[].content").description("댓글 내용"),
                                fieldWithPath("comments.[].postId").description("게시글 id"),
                                fieldWithPath("comments.[].member.id").description("멤버 id"),
                                fieldWithPath("comments.[].member.nickname").description("닉네임"),
                                fieldWithPath("comments.[].likeCount").description("좋아요 갯수"),
                                fieldWithPath("comments.[].createDate").description("생성 날짜"),
                                fieldWithPath("comments.[].modifyDate").description("수정 날짜"),
                                fieldWithPath("comments.[].replies").description("대댓글"),
                                fieldWithPath("comments.[].replies.[].id").description("댓글 id"),
                                fieldWithPath("comments.[].replies.[].postId").description("게시글 id"),
                                fieldWithPath("comments.[].replies.[].content").description("댓글 내용"),
                                fieldWithPath("comments.[].replies.[].member.id").description("멤버 id"),
                                fieldWithPath("comments.[].replies.[].member.nickname").description("닉네임"),
                                fieldWithPath("comments.[].replies.[].likeCount").description("좋아요 갯수"),
                                fieldWithPath("comments.[].replies.[].createDate").description("생성 날짜"),
                                fieldWithPath("comments.[].replies.[].modifyDate").description("수정 날짜"),
                                fieldWithPath("comments.[].replies.[].liked").description("좋아여 여부"),
                                fieldWithPath("comments.[].replies.[].owner").description("본인 댓글 여부"),
                                fieldWithPath("comments.[].deleted").description("본인 댓글 여부"),
                                fieldWithPath("comments.[].liked").description("부모 댓글 삭제 여부"),
                                fieldWithPath("comments.[].owner").description("본인 댓글 여부"),
                                fieldWithPath("totalCommentCount").description("댓글 갯수"),
                                fieldWithPath("totalPage").description("게시글 총 페이지")
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
    @DisplayName("대댓글을 성공적으로 생성한다.")
    void createChildrenComment() throws Exception {
        // given
        ChildrenCreateRequest childrenCreateRequest = ChildrenCommentCreateRequestBuilder.build();

        given(commentService.createChildrenComment(any(), any(), any(), any())).willReturn(1L);

        // when
        ResultActions resultActions = requestCreateChildrenComment(childrenCreateRequest);

        // then
        resultActions
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/posts/1/reply/1"))
                .andDo(document("Comment/ChildrenCreate",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestFields(
                                fieldWithPath("content").description("대댓글 내용")
                        )
                ));
    }

    @Test
    @DisplayName("댓글 길이가 250자를 넘었을 경우 에러가 발생한다.")
    void CreateComment_length_denied() throws Exception {
        // given
        CommentCreateRequest commentCreateRequest = CommentCreate411RequestBuilder.build();

        willThrow(new CommentInputException()).given(commentService).createComment(any(),any(),any());

        // when
        ResultActions resultActions = requestCreateComment(commentCreateRequest);

        // then
        resultActions
                .andExpect(status().isLengthRequired())
                .andDo(document("comment/create/length-denied",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        responseFields(
                                fieldWithPath("message").description("에러 메세지"),
                                fieldWithPath("errors").description("에러 종류"),
                                fieldWithPath("code").description("코드")
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
    @DisplayName("댓글 수정 권한이 없을 경우 예외가 발생한다.")
    void modifyComment_access_denied() throws Exception {
        // given
        CommentModifyRequest commentModifyRequest = CommentModifyRequestBuilder.build();

        willThrow(new CommentAccessDeniedException()).given(commentService).modifyComment(any(),any(),any(), any());

        // when
        ResultActions resultActions = requestModifyComment(commentModifyRequest);

        // then
        resultActions
                .andExpect(status().isForbidden())
                .andDo(document("comment/modify/access-denied",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        responseFields(
                                fieldWithPath("message").description("에러 메세지"),
                                fieldWithPath("errors").description("에러 종류"),
                                fieldWithPath("code").description("코드")
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

    @Test
    @DisplayName("댓글 삭제 권한이 없을 경우 예외가 발생한다.")
    void deleteComment_access_denied() throws Exception {
        // given
        willThrow(new CommentAccessDeniedException()).given(commentService).deleteComment(any(),any(),any());

        // when
        ResultActions resultActions = requestDeleteComment("/posts/1/comments/1");

        // then
        resultActions
                .andExpect(status().isForbidden())
                .andDo(document("comment/delete/access-denied",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        responseFields(
                                fieldWithPath("message").description("에러 메세지"),
                                fieldWithPath("errors").description("에러 종류"),
                                fieldWithPath("code").description("코드")
                        )
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

    private ResultActions requestCreateChildrenComment(ChildrenCreateRequest dto) throws Exception {
        return mockMvc.perform(post("/posts/1/reply/1")
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