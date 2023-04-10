package com.masil.domain.commentlike.controller;

import com.masil.common.annotation.ControllerMockApiTest;
import com.masil.domain.commentlike.dto.CommentLikeResponse;
import com.masil.domain.commentlike.exception.SelfCommentLikeException;
import com.masil.domain.commentlike.service.CommentLikeService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.ResultActions;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest({
        CommentLikeController.class,
})
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("test")
class CommentLikeControllerTest extends ControllerMockApiTest {

    @MockBean
    private CommentLikeService commentLikeService;

    private static final String AUTHORIZATION_HEADER_VALUE = "Bearer aaaaaaaa.bbbbbbbb.cccccccc";

    @Test
    @DisplayName("댓글 좋아요 성공")
    void likeComment_success() throws Exception {
        // given
        given(commentLikeService.updateLikeOfComment(any(), any())).willReturn(CommentLikeResponse.of(1, true));

        // when
        ResultActions resultActions = requestLikeComment("/api/comments/1/modify-like");
        // then
        resultActions
                .andExpect(status().is2xxSuccessful())
                .andDo(document("comment/commentLike",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        responseFields(
                                fieldWithPath("likeCount").description("좋아요 개수"),
                                fieldWithPath("status").description("좋아요 여부")
                        )
                ));
    }

    @Test
    @DisplayName("본인 댓글에 좋아요 불가능하다.")
    void addLikeComment_mine_denied() throws Exception {
        willThrow(new SelfCommentLikeException()).given(commentLikeService).updateLikeOfComment(any(),any());

        // when
        ResultActions resultActions = requestLikeComment("/api/comments/1/modify-like");

        // then
        resultActions
                .andExpect(status().isForbidden())
                .andDo(document("comment/commentLike/self-denied",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        responseFields(
                                fieldWithPath("message").description("에러 메세지"),
                                fieldWithPath("errors").description("에러 종류"),
                                fieldWithPath("code").description("코드")
                        )
                ));
    }

    private ResultActions requestLikeComment(String url) throws Exception {
        return mockMvc.perform(put(url)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, AUTHORIZATION_HEADER_VALUE))
                .andDo(print());
    }
}