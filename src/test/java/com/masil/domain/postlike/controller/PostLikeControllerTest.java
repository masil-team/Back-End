package com.masil.domain.postlike.controller;

import com.masil.common.annotation.ControllerMockApiTest;
import com.masil.domain.postlike.dto.PostLikeResponse;
import com.masil.domain.postlike.exception.SelfPostLikeException;
import com.masil.domain.postlike.service.PostLikeService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest({
        PostLikeController.class,
})
@AutoConfigureMockMvc(addFilters = false)
class PostLikeControllerTest extends ControllerMockApiTest {

    @MockBean
    private PostLikeService postLikeService;

    @Test
    @DisplayName("게시글 좋아요 성공")
    void likePost_success() throws Exception {
        // given
        given(postLikeService.toggleLikePost(any(), any()))
                .willReturn(PostLikeResponse.of(1, true));

        // when
        ResultActions resultActions = requestLikePost("/api/posts/1/modify-like");

        // then
        resultActions
                .andExpect(status().is2xxSuccessful())
                .andDo(document("post/like",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        responseFields(
                                fieldWithPath("likeCount").description("좋아요 개수"),
                                fieldWithPath("isLike").description("좋아요 여부")
                        )
                ));
    }

    @Test
    @DisplayName("본인 글에 좋아요를 누를 경우 예외가 발생한다.")
    void likePost_selfLike() throws Exception {
        // given
        given(postLikeService.toggleLikePost(any(), any()))
                .willThrow(new SelfPostLikeException());

        // when
        ResultActions resultActions = requestLikePost("/api/posts/1/modify-like");

        // then
        resultActions
                .andExpect(status().isForbidden())
                .andDo(document("post/like/selfLike",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        responseFields(
                                fieldWithPath("message").description("에러 메세지"),
                                fieldWithPath("errors").description("에러 종류"),
                                fieldWithPath("code").description("코드")
                        )
                ));
    }

    private ResultActions requestLikePost(String url) throws Exception {
        return mockMvc.perform(put(url)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print());
    }
}