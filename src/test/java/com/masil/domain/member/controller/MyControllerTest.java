package com.masil.domain.member.controller;

import com.masil.common.annotation.ControllerMockApiTest;
import com.masil.domain.bookmark.service.BookmarkService;
import com.masil.domain.comment.service.CommentService;
import com.masil.domain.commentlike.service.CommentLikeService;
import com.masil.domain.member.dto.request.MyFindRequest;
import com.masil.domain.member.dto.response.MemberResponse;
import com.masil.domain.post.dto.PostsElementResponse;
import com.masil.domain.post.dto.PostsResponse;
import com.masil.domain.post.service.PostService;
import com.masil.domain.postlike.service.PostLikeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.everyItem;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest({
        MyController.class,
})
@AutoConfigureMockMvc(addFilters = false)
class MyControllerTest extends ControllerMockApiTest {

    @MockBean
    private PostService postService;

    @MockBean
    private CommentService commentService;

    @MockBean
    private BookmarkService bookmarkService;

    @MockBean
    private PostLikeService postLikeService;

    @MockBean
    private CommentLikeService commentLikeService;

    private PostsResponse normalPostsResponse;

    private PostsResponse myLikesPostsResponse;

    private PostsResponse myBookmarkPostsResponse;


    @BeforeEach
    void setUp() {
        List<PostsElementResponse> posts = new ArrayList<>();
        for (int i = 1; i < 5; i++) {
            posts.add(PostsElementResponse.builder()
                    .id((long) i)
                    .member(MemberResponse.builder()
                            .id(1L)
                            .nickname("회원A")
                            .build())
                    .boardId((long) i)
                    .address("A 주소")
                    .content(i + "번째 글 내용")
                    .viewCount(i)
                    .likeCount(i)
                    .commentCount(i)
                    .isOwner(true)
                    .isLiked(true)
                    .isScrap(true)
                    .createDate(LocalDateTime.now())
                    .modifyDate(null)
                    .build());
        }

        normalPostsResponse = new PostsResponse(posts, true);
    }
    @Test
    @DisplayName("내가 쓴 글 조회")
    void findMyPostsTestSuccess() throws Exception {

        //given
        MyFindRequest request = MyFindRequest.builder().memberId(1L).build();
        given(postService.findPostsByMember(any(), any())).willReturn(normalPostsResponse);

        //expected
        mockMvc.perform(get("/my/posts")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.posts[*].member.id",
                        everyItem(equalTo(1))))
                .andExpect(jsonPath("$.posts[*].member.nickname",
                        everyItem(equalTo("회원A"))))
                .andDo(print());

        verify(postService, times(1)).findPostsByMember(any(), any());
    }
}