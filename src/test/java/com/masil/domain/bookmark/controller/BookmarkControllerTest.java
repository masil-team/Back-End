package com.masil.domain.bookmark.controller;

import com.masil.common.annotation.ControllerMockApiTest;
import com.masil.domain.bookmark.dto.BookmarkResponse;
import com.masil.domain.bookmark.service.BookmarkService;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest({
        BookmarkController.class,
})
@AutoConfigureMockMvc(addFilters = false)
class BookmarkControllerTest extends ControllerMockApiTest {

    @MockBean
    private BookmarkService bookmarkService;

    private static final String AUTHORIZATION_HEADER_VALUE = "Bearer aaaaaaaa.bbbbbbbb.cccccccc";

    @Test
    @DisplayName("즐겨찾기를 성공적으로 추가한다")
    void addBookmark() throws Exception {

        // given
        given(bookmarkService.addBookmark(any(), any())).willReturn(BookmarkResponse.of(true));

        // when
        ResultActions resultActions = requestAddBookmark("/posts/1/bookmark");

        // then
        resultActions
                .andExpect(status().is2xxSuccessful())
                .andDo(document("bookmark/add",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        responseFields(
                                fieldWithPath("isScrap").description("스크랩 여부")
                        )
                ));
    }

    @Test
    @DisplayName("즐겨찾기를 성공적으로 삭제한다")
    void deleteBookmark() throws Exception {

        // given
        given(bookmarkService.deleteBookmark(any(), any())).willReturn(BookmarkResponse.of(false));

        // when
        ResultActions resultActions = requestDeleteBookmark("/posts/1/bookmark");

        // then
        resultActions
                .andExpect(status().is2xxSuccessful())
                .andDo(document("bookmark/delete",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        responseFields(
                                fieldWithPath("isScrap").description("스크랩 여부")
                        )
                ));
    }

    private ResultActions requestAddBookmark(String url) throws Exception {
        return mockMvc.perform(post(url)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print());
    }

    private ResultActions requestDeleteBookmark(String url) throws Exception {
        return mockMvc.perform(delete(url)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print());
    }

}