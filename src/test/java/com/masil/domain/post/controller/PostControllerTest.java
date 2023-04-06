package com.masil.domain.post.controller;


import com.masil.common.annotation.ControllerMockApiTest;
import com.masil.domain.member.dto.response.MemberResponse;
import com.masil.domain.post.dto.*;
import com.masil.domain.post.exception.PostAccessDeniedException;
import com.masil.domain.post.exception.PostNotFoundException;
import com.masil.domain.post.service.PostService;
import com.masil.domain.storage.dto.FileResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
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
        PostController.class,
})
@AutoConfigureMockMvc(addFilters = false)
public class PostControllerTest extends ControllerMockApiTest {

    @MockBean
    private PostService postService;

    private static final MemberResponse MEMBER_RESPONSE = MemberResponse.builder()
            .id(1L)
            .nickname("닉네임1")
            .build();

    private static final FileResponse FILE_RESPONSE = FileResponse.builder()
            .url("https://hello.net/post-image/hello.jpg")
            .height(240)
            .width(240)
            .build();

    private static final String AUTHORIZATION_HEADER_VALUE = "Bearer aaaaaaaa.bbbbbbbb.cccccccc";

    @Test
    @DisplayName("게시글 생성을 성공한다.")
    void createPost_success() throws Exception {
        // given
        PostCreateRequest postCreateRequest = PostCreateRequestBuilder.build();

        given(postService.createPost(any(), any())).willReturn(1L);

        // when
        ResultActions resultActions = requestCreatePost("/api/posts", postCreateRequest);

        // then
        resultActions
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/api/posts/1"))
                .andDo(document("post/create",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestFields(
                                fieldWithPath("content").description("내용"),
                                fieldWithPath("boardId").description("카테고리Id"),
                                fieldWithPath("fileIds.[]").description("파일 id")
                        )
                ));
    }

    @Test
    @DisplayName("게시글 상세 조회를 성공한다.")
    void findPost_success() throws Exception {

        // given
        List<FileResponse> files = new ArrayList<>();
        files.add(FILE_RESPONSE);
        PostDetailResponse POST_RESPONSE_1 = PostDetailResponse.builder()
                .id(1L)
                .member(MEMBER_RESPONSE)
                .boardId(1L)
                .address("옥천동")
                .content("내용1")
                .viewCount(0)
                .likeCount(0)
                .isOwner(false)
                .isLiked(false)
                .isScrap(false)
                .createDate(LocalDateTime.now())
                .modifyDate(LocalDateTime.now())
                .imageCount(files.size())
                .files(files)
                .build();
        given(postService.findDetailPost(any(), any())).willReturn(POST_RESPONSE_1);

        // when
        ResultActions resultActions = requestFindPost("/api/guest-available/posts/1");

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
                                fieldWithPath("boardId").description("카테고리Id"),
                                fieldWithPath("address").description("주소"),
                                fieldWithPath("content").description("내용"),
                                fieldWithPath("viewCount").description("조회수"),
                                fieldWithPath("likeCount").description("좋아요 개수"),
                                fieldWithPath("isOwner").description("본인 게시글 여부"),
                                fieldWithPath("isLiked").description("게시글 좋아요 여부"),
                                fieldWithPath("isScrap").description("즐겨찾기 여부"),
                                fieldWithPath("createDate").description("생성 날짜"),
                                fieldWithPath("modifyDate").description("수정 날짜"),
                                fieldWithPath("imageCount").description("이미지 개수"),
                                fieldWithPath("files.[].url").description("이미지 주소"),
                                fieldWithPath("files.[].width").description("너비"),
                                fieldWithPath("files.[].height").description("높이")
                        )
                ));
    }
    @Test
    @DisplayName("존재하지 않는 게시글일 경우 예외가 발생한다.")
    void findPost_notFound() throws Exception {

        // given
        given(postService.findDetailPost(any(), any())).willThrow(new PostNotFoundException());

        // when
        ResultActions resultActions = requestFindPost("/api/guest-available/posts/99");

        // then
        resultActions
                .andExpect(status().isNotFound())
                .andDo(document("post/findOne/notFound",
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
    @DisplayName("게시글 목록 조회를 성공한다.")
    void findAllPost_success() throws Exception {
        // given
        List<PostsElementResponse> postsElementResponseList = new ArrayList<>();

        postsElementResponseList.add(PostsElementResponse.builder()
                .id(1L)
                .member(MEMBER_RESPONSE)
                .boardId(1L)
                .address("옥천동")
                .content("내용")
                .viewCount(0)
                .likeCount(0)
                .commentCount(0)
                .isOwner(false)
                .isLiked(false)
                .isScrap(false)
                .createDate(LocalDateTime.now())
                .modifyDate(LocalDateTime.now())
                .thumbnail(FILE_RESPONSE)
                .build());


        PostsResponse postsResponse = new PostsResponse(postsElementResponseList, true);

        given(postService.findPosts(any(), any())).willReturn(postsResponse);

        // when
        ResultActions resultActions = requestFindAllPost("/api/guest-available/boards/1/posts?rCode=11680&page=0&size=8");

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
                                fieldWithPath("posts.[].boardId").description("카테고리Id"),
                                fieldWithPath("posts.[].address").description("주소"),
                                fieldWithPath("posts.[].content").description("내용"),
                                fieldWithPath("posts.[].viewCount").description("조회수"),
                                fieldWithPath("posts.[].likeCount").description("좋아요 개수"),
                                fieldWithPath("posts.[].commentCount").description("댓글 개수"),
                                fieldWithPath("posts.[].isOwner").description("본인 글 여부"),
                                fieldWithPath("posts.[].isLiked").description("좋아요 여부"),
                                fieldWithPath("posts.[].isScrap").description("즐겨찾기 여부"),
                                fieldWithPath("posts.[].createDate").description("생성 날짜"),
                                fieldWithPath("posts.[].modifyDate").description("수정 날짜"),
                                fieldWithPath("posts.[].thumbnail.url").description("이미지 주소"),
                                fieldWithPath("posts.[].thumbnail.width").description("너비"),
                                fieldWithPath("posts.[].thumbnail.height").description("높이"),
                                fieldWithPath("isLast").description("마지막 페이지 여부")
                        )
                ));
    }

    @Test
    @DisplayName("게시글 수정")
    void modifyPost_success() throws Exception {
        // given
        PostModifyRequest postModifyRequest = PostModifyRequestBuilder.build();

        willDoNothing().given(postService).modifyPost(any(),any(),any());

        // when
        ResultActions resultActions = requestModifyPost("/api/posts/1" ,postModifyRequest);

        // then
        resultActions
                .andExpect(status().is2xxSuccessful())
                .andDo(document("post/modify",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestFields(
                                fieldWithPath("content").description("수정할 내용"),
                                fieldWithPath("boardId").description("카테고리Id")
                        )
                ));
    }

    @Test
    @DisplayName("게시글 수정 권한이 없을 경우 예외가 발생한다.")
    void modifyPost_access_denied() throws Exception {
        // given
        PostModifyRequest postModifyRequest = PostModifyRequestBuilder.build();

        willThrow(new PostAccessDeniedException()).given(postService).modifyPost(any(),any(),any());

        // when
        ResultActions resultActions = requestModifyPost("/api/posts/1" ,postModifyRequest);

        // then
        resultActions
                .andExpect(status().isForbidden())
                .andDo(document("post/modify/access-denied",
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
    @DisplayName("게시글 삭제")
    void deletePost_success() throws Exception {

        willDoNothing().given(postService).deletePost(any(), any());
        // when
        ResultActions resultActions = requestDeletePost("/api/posts/1");

        // then
        resultActions
                .andExpect(status().isNoContent())
                .andDo(document("post/delete",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint())
                ));
    }

    @Test
    @DisplayName("게시글 삭제 권한이 없을 경우 예외가 발생한다.")
    void deletePost_access_denied() throws Exception {

        // given
        willThrow(new PostAccessDeniedException()).given(postService).deletePost(any(), any());

        // when
        ResultActions resultActions = requestDeletePost("/api/posts/1");

        // then
        resultActions
                .andExpect(status().isForbidden())
                .andDo(document("post/delete/access_denied",
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
    @DisplayName("게시글 검색 조회를 성공한다.")
    void searchPost_success() throws Exception {
        // given
        List<PostsElementResponse> postsElementResponseList = new ArrayList<>();

        postsElementResponseList.add(PostsElementResponse.builder()
                .id(1L)
                .member(MEMBER_RESPONSE)
                .boardId(1L)
                .address("용문동")
                .content("잠실")
                .viewCount(0)
                .likeCount(0)
                .commentCount(0)
                .isOwner(false)
                .isLiked(false)
                .isScrap(false)
                .createDate(LocalDateTime.now())
                .modifyDate(LocalDateTime.now())
                .thumbnail(FILE_RESPONSE)
                .build());


        PostsResponse postsResponse = new PostsResponse(postsElementResponseList, true);

        given(postService.searchPosts(any(), any(), any())).willReturn(postsResponse);

        // when
        ResultActions resultActions = searchPost("/api/guest-available/posts/search?keyword=내용&&rCode=11170122");

        // then
        resultActions
                .andExpect(status().is2xxSuccessful())
                .andDo(document("post/search",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        responseFields(
                                fieldWithPath("posts.[].id").description("게시글 id"),
                                fieldWithPath("posts.[].member.id").description("작성자 id"),
                                fieldWithPath("posts.[].member.nickname").description("닉네임"),
                                fieldWithPath("posts.[].boardId").description("카테고리Id"),
                                fieldWithPath("posts.[].address").description("주소"),
                                fieldWithPath("posts.[].content").description("내용"),
                                fieldWithPath("posts.[].viewCount").description("조회수"),
                                fieldWithPath("posts.[].likeCount").description("좋아요 개수"),
                                fieldWithPath("posts.[].commentCount").description("댓글 개수"),
                                fieldWithPath("posts.[].isOwner").description("본인 글 여부"),
                                fieldWithPath("posts.[].isLiked").description("좋아요 여부"),
                                fieldWithPath("posts.[].isScrap").description("즐겨찾기 여부"),
                                fieldWithPath("posts.[].createDate").description("생성 날짜"),
                                fieldWithPath("posts.[].modifyDate").description("수정 날짜"),
                                fieldWithPath("posts.[].thumbnail.url").description("이미지 주소"),
                                fieldWithPath("posts.[].thumbnail.width").description("너비"),
                                fieldWithPath("posts.[].thumbnail.height").description("높이"),
                                fieldWithPath("isLast").description("마지막 페이지 여부")
                        )
                ));
    }

    private ResultActions requestCreatePost(String url, PostCreateRequest dto) throws Exception {
        return mockMvc.perform(post(url)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
//                        .header(HttpHeaders.AUTHORIZATION, AUTHORIZATION_HEADER_VALUE)
                        .content(objectMapper.writeValueAsString(dto)))
                .andDo(print());
    }

    private ResultActions requestFindPost(String url) throws Exception {
        return mockMvc.perform(get(url)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON))
//                        .header(HttpHeaders.AUTHORIZATION, AUTHORIZATION_HEADER_VALUE))
                .andDo(print());
    }

    private ResultActions requestFindAllPost(String url) throws Exception {
        return mockMvc.perform(get(url)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print());
    }
    private ResultActions requestModifyPost(String url, PostModifyRequest dto) throws Exception {
        return mockMvc.perform(patch(url)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
//                        .header(HttpHeaders.AUTHORIZATION, AUTHORIZATION_HEADER_VALUE)
                        .content(objectMapper.writeValueAsString(dto)))
                .andDo(print());
    }
    private ResultActions requestDeletePost(String url) throws Exception {
        return mockMvc.perform(delete(url)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON))
//                        .header(HttpHeaders.AUTHORIZATION, AUTHORIZATION_HEADER_VALUE))
                .andDo(print());
    }
    private ResultActions searchPost(String url) throws Exception {
        return mockMvc.perform(get(url)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print());
    }
}
