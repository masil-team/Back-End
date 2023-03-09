package com.masil.domain.member.controller;

import com.masil.common.annotation.ControllerMockApiTest;
import com.masil.domain.bookmark.service.BookmarkService;
import com.masil.domain.comment.dto.ChildrenResponse;
import com.masil.domain.comment.dto.CommentResponse;
import com.masil.domain.comment.dto.CommentsResponse;
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

    private CommentsResponse myCommentsResponse;
    private CommentsResponse myLikeCommentsResponse;

    private MemberResponse memberResponseA;
    private MemberResponse memberResponseB;
    @BeforeEach
    void setUp() {
        setUpMember();
        setUpNormalPostsResponse();
        setUpMyCommentsResponse();
        setUpMyLikesPostsResponse();
        setUpMyLikesCommentsResponse();
        setUpMyBookmarkPostsResponse();
    }

    private void setUpMyBookmarkPostsResponse() {
        List<PostsElementResponse> posts = new ArrayList<>();
        for (int i = 1; i < 5; i++) {
            posts.add(PostsElementResponse.builder()
                    .id((long) i)
                    .member(memberResponseB)
                    .boardId((long) i)
                    .address("A 주소")
                    .content(i + "번째 글 내용")
                    .viewCount(i)
                    .likeCount(i)
                    .commentCount(i)
                    .isOwner(false)
                    .isLiked(false)
                    .isScrap(true)
                    .createDate(LocalDateTime.now())
                    .modifyDate(null)
                    .build());
        }
        myBookmarkPostsResponse = new PostsResponse(posts, true);
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

    @Test
    @DisplayName("내 댓글 조회")
    void myCommentsSuccess() throws Exception {

        //given
        given(commentService.findCommentsByMemberId(any(), any())).willReturn(myCommentsResponse);
        MyFindRequest request = MyFindRequest.builder().memberId(1L).build();

        //expected
        mockMvc.perform(get("/my/comments")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.comments[*].member.id",
                        everyItem(equalTo(1))))
                .andExpect(jsonPath("$.comments[*].member.nickname",
                        everyItem(equalTo("회원A"))))
                .andExpect(jsonPath("$.comments[1].replies[*].member.id",
                        everyItem(equalTo(1))))
                .andExpect(jsonPath("$.comments[1].replies[*].member.nickname",
                        everyItem(equalTo("회원A"))))
                .andDo(print());

        verify(commentService, times(1)).findCommentsByMemberId(any(), any());
    }

    @Test
    @DisplayName("내가 좋아요 누른 글 조회")
    void findMyLikesPostsSuccess () throws Exception {

        //given
        given(postLikeService.findLikesByMemberId(any(), any())).willReturn(myLikesPostsResponse);
        MyFindRequest request = MyFindRequest.builder().memberId(1L).build();

        //expected
        mockMvc.perform(get("/my/post-likes")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.posts[*].isLiked",
                        everyItem(equalTo(true))))
                .andDo(print());

        verify(postLikeService, times(1)).findLikesByMemberId(any(), any());

    }

    @Test
    @DisplayName("내가 좋아요 누른 댓글 조회")
    void findMyLikesCommentsSuccess () throws Exception {

        //given
        given(commentLikeService.findLikesByMemberId(any(), any())).willReturn(myLikeCommentsResponse);
        MyFindRequest request = MyFindRequest.builder().memberId(1L).build();

        //expected
        mockMvc.perform(get("/my/comment-likes")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.comments[*].isLiked",
                        everyItem(equalTo(true))))
                .andDo(print());

        verify(commentLikeService, times(1)).findLikesByMemberId(any(), any());

    }

    @Test
    @DisplayName("내가 즐겨찾기한 글 조회")
    void findMyBookmarksSuccess () throws Exception {

        //given
        given(bookmarkService.findBookmarksByMember(any(), any())).willReturn(myBookmarkPostsResponse);
        MyFindRequest request = MyFindRequest.builder().memberId(1L).build();

        //expected
        mockMvc.perform(get("/my/bookmarks")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.posts[*].isScrap",
                        everyItem(equalTo(true))))
                .andDo(print());

        verify(bookmarkService, times(1)).findBookmarksByMember(any(), any());
    }

    private void setUpMyCommentsResponse() {
        myCommentsResponse = CommentsResponse.builder()
                .comments(getCommentResponses())
                .totalCommentCount(5L)
                .totalPage(1)
                .isLast(true)
                .build();
    }

    private void setUpMyLikesCommentsResponse() {
        myLikeCommentsResponse = CommentsResponse.builder()
                .comments(getLikesCommentResponses())
                .totalCommentCount(5L)
                .totalPage(1)
                .isLast(true)
                .build();
    }

    private List<CommentResponse> getLikesCommentResponses() {
        List<CommentResponse> commentResponses = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            if (i == 1) {
                commentResponses.add(
                        CommentResponse.builder()
                                .id((long) i + 5)
                                .content(i + "번째 부모댓글")
                                .postId(1L)
                                .member(memberResponseA)
                                .isOwner(true)
                                .likeCount(0)
                                .isLiked(true)
                                .createDate(LocalDateTime.now())
                                .modifyDate(null)
                                .isDeleted(false)
                                .replies(likesReplies())
                                .build()
                );
            }
            commentResponses.add(
                    CommentResponse.builder()
                            .id((long) i + 5)
                            .content(i + "부모댓글")
                            .postId((long) i)
                            .member(memberResponseB)
                            .isOwner(false)
                            .likeCount(0)
                            .isLiked(true)
                            .createDate(LocalDateTime.now())
                            .modifyDate(null)
                            .isDeleted(false)
                            .build()
            );
        }
        return commentResponses;
    }

    private List<CommentResponse> getCommentResponses() {
        List<CommentResponse> commentResponses = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            if (i == 1) {
                commentResponses.add(
                        CommentResponse.builder()
                                .id((long) i + 5)
                                .content(i + "번째 부모댓글")
                                .postId(1L)
                                .member(memberResponseA)
                                .isOwner(true)
                                .likeCount(0)
                                .isLiked(false)
                                .createDate(LocalDateTime.now())
                                .modifyDate(null)
                                .isDeleted(false)
                                .replies(commentReplies())
                                .build()
                );
            }
            commentResponses.add(
                    CommentResponse.builder()
                            .id((long) i + 5)
                            .content(i + "부모댓글")
                            .postId((long) i)
                            .member(memberResponseA)
                            .isOwner(true)
                            .likeCount(0)
                            .isLiked(false)
                            .createDate(LocalDateTime.now())
                            .modifyDate(null)
                            .isDeleted(false)
                            .build()
            );
        }
        return commentResponses;
    }

    private List<ChildrenResponse> commentReplies() {
        List<ChildrenResponse> replies = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            replies.add(ChildrenResponse.builder()
                    .id((long) i)
                    .content(i + "번째 대댓글")
                    .postId(1L)
                    .member(memberResponseA)
                    .isOwner(true)
                    .likeCount(1)
                    .isLiked(true)
                    .createDate(LocalDateTime.now())
                    .modifyDate(null)
                    .build());
        }
        return replies;
    }

    private List<ChildrenResponse> likesReplies() {
        List<ChildrenResponse> replies = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            replies.add(ChildrenResponse.builder()
                    .id((long) i)
                    .content(i + "번째 대댓글")
                    .postId(1L)
                    .member(memberResponseB)
                    .isOwner(false)
                    .likeCount(1)
                    .isLiked(true)
                    .createDate(LocalDateTime.now())
                    .modifyDate(null)
                    .build());
        }
        return replies;
    }

    private void setUpMember() {
        memberResponseA = MemberResponse.builder()
                .id(1L)
                .nickname("회원A")
                .build();

        memberResponseB = MemberResponse.builder()
                .id(2L)
                .nickname("회원B")
                .build();
    }

    private void setUpNormalPostsResponse() {
        List<PostsElementResponse> posts = new ArrayList<>();
        for (int i = 1; i < 5; i++) {
            posts.add(PostsElementResponse.builder()
                    .id((long) i)
                    .member(memberResponseA)
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

    private void setUpMyLikesPostsResponse() {
        List<PostsElementResponse> posts = new ArrayList<>();
        for (int i = 1; i < 5; i++) {
            posts.add(PostsElementResponse.builder()
                    .id((long) i)
                    .member(memberResponseB)
                    .boardId(1L)
                    .address("B 주소")
                    .content(i + "번째 글 내용")
                    .viewCount(i)
                    .likeCount(i)
                    .commentCount(i)
                    .isOwner(false)
                    .isLiked(true)
                    .isScrap(false)
                    .createDate(LocalDateTime.now())
                    .modifyDate(null)
                    .build());
        }
        for (int i = 5; i < 10; i++) {
            posts.add(PostsElementResponse.builder()
                    .id((long) i)
                    .member(memberResponseA)
                    .boardId(1L)
                    .address("A 주소")
                    .content(i + "번째 글 내용")
                    .viewCount(i)
                    .likeCount(i)
                    .commentCount(i)
                    .isOwner(true)
                    .isLiked(true)
                    .isScrap(false)
                    .createDate(LocalDateTime.now())
                    .modifyDate(null)
                    .build());
        }
        myLikesPostsResponse = new PostsResponse(posts, true);
    }
}