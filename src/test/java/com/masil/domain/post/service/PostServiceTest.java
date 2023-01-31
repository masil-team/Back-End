package com.masil.domain.post.service;

import com.masil.common.annotation.ServiceTest;
import com.masil.domain.board.entity.Board;
import com.masil.domain.board.repository.BoardRepository;
import com.masil.domain.member.entity.Member;
import com.masil.domain.member.repository.MemberRepository;
import com.masil.domain.post.dto.*;
import com.masil.domain.post.entity.Post;
import com.masil.domain.post.entity.State;
import com.masil.domain.post.exception.PostAccessDeniedException;
import com.masil.domain.post.exception.PostNotFoundException;
import com.masil.domain.post.repository.PostRepository;
import com.masil.global.error.exception.BusinessException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.annotation.Commit;

import java.awt.print.Pageable;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.springframework.data.domain.Sort.Direction.DESC;

public class PostServiceTest extends ServiceTest {

    @Autowired
    private PostService postService;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private PostRepository postRepository;
    @Autowired
    private BoardRepository boardRepository;

    private static final String POST_CONTENT_1 = "내용1";
    private static final String POST_CONTENT_2 = "내용2";
    private static final String USER_EMAIL_1 = "email1@naver.com";
    private static final String USER_EMAIL_2 = "email2@naver.com";
    private static final String USER_NICKNAME_1 = "test1";
    private static final String USER_NICKNAME_2 = "test2";

    @BeforeEach
    void setUp() {
        Board board = Board.builder()
                .id(1L)
                .name("ALL")
                .build();
        boardRepository.save(board);
        Member member1 = Member.builder()
                .email(USER_EMAIL_1)
                .nickname(USER_NICKNAME_1)
                .password("123")
                .build();
        Member member2 = Member.builder()
                .email(USER_EMAIL_2)
                .nickname(USER_NICKNAME_2)
                .password("123")
                .build();
        memberRepository.save(member1);
        memberRepository.save(member2);

        Post post1 = Post.builder()
                .content(POST_CONTENT_1)
                .member(member1)
                .board(board)
                .build();
        Post post2 = Post.builder()
                .content(POST_CONTENT_2)
                .member(member1)
                .board(board)
                .build();
        postRepository.save(post1);
        postRepository.save(post2);
    }

    @DisplayName("상세 게시글이 성공적으로 조회된다.")
    @Test
    void findPost_success() {

        // when
        PostDetailResponse postDetailResponse = postService.findDetailPost(1L, 2L);

        // then
        assertThat(postDetailResponse.getId()).isEqualTo(1L);
        assertThat(postDetailResponse.getMember().getId()).isEqualTo(1L);
        assertThat(postDetailResponse.getMember().getNickname()).isEqualTo(USER_NICKNAME_1);
        assertThat(postDetailResponse.getContent()).isEqualTo(POST_CONTENT_1);
        assertThat(postDetailResponse.getViewCount()).isEqualTo(1);
        assertThat(postDetailResponse.getLikeCount()).isEqualTo(0);
        assertThat(postDetailResponse.getIsOwner()).isEqualTo(false);
        assertThat(postDetailResponse.getIsLike()).isEqualTo(false);
    }

    @DisplayName("존재하지 않는 게시글일 경우 예외가 발생한다")
    @Test
    void findPost_notFound() {

        // when, then
        assertThatThrownBy(() -> postService.findDetailPost(100L, 2L))
                .isInstanceOf(PostNotFoundException.class);
    }

    @DisplayName("본인의 상세 게시글인 경우")
    @Test
    void findPost_isOwner() {

        // when
        PostDetailResponse postDetailResponse = postService.findDetailPost(1L, 1L);

        // then
        assertThat(postDetailResponse.getIsOwner()).isEqualTo(true);
    }

    @DisplayName("좋아요한 상세 게시글인 경우")
    @Test
    void findPost_isLike() {
        /**
         *
         */
    }

    @DisplayName("게시글 목록이 성공적으로 조회된다.")
    @Test
    void findAllPost_success() {

        // when
        PostsResponse allPost = postService.findAllPost(1L, PageRequest.of(0, 8, DESC, "createDate"));
        List<PostsElementResponse> postList = allPost.getPosts();
        PostsElementResponse postsElementResponse = postList.get(postList.size()-1);

        // then
        assertThat(allPost.getPosts().size()).isEqualTo(2);
        assertThat(postsElementResponse.getId()).isEqualTo(1L);
        assertThat(postsElementResponse.getMember().getId()).isEqualTo(1L);
        assertThat(postsElementResponse.getMember().getNickname()).isEqualTo(USER_NICKNAME_1);
        assertThat(postsElementResponse.getContent()).isEqualTo(POST_CONTENT_1);
        assertThat(postsElementResponse.getViewCount()).isEqualTo(0);
        assertThat(postsElementResponse.getLikeCount()).isEqualTo(0);
        assertThat(postsElementResponse.getCommentCount()).isEqualTo(0);
    }

    @DisplayName("게시글이 성공적으로 생성된다.")
    @Test
    void createPost_test() {
        // given
        Member member = memberRepository.findById(1L).get();

        String content = "새로운 내용";
        PostCreateRequest postCreateRequest = PostCreateRequestBuilder.build(content);
        // when
        Long postId = postService.createPost(postCreateRequest, 1L);
        Post post = postRepository.findById(postId).get();


        // then
        assertThat(post.getId()).isEqualTo(postId);
        assertThat(post.getContent()).isEqualTo(content);
        assertThat(post.getMember().getId()).isEqualTo(member.getId());
        assertThat(post.getViewCount()).isEqualTo(0);
        assertThat(post.getBoard().getId()).isEqualTo(1L);
        assertThat(post.getState()).isEqualTo(State.NORMAL);
    }

    @DisplayName("게시글 수정이 성공적으로 수행된다.")
    @Test
    void modifyPost_success() {

        // given
        Member member = memberRepository.findById(1L).get();

        String content = "수정 후 내용";
        Post beforePost = postRepository.findById(1L).get();

        PostModifyRequest postModifyRequest = PostModifyRequestBuilder.build(content);

        // when
        postService.modifyPost(beforePost.getId(), postModifyRequest, member.getId());

        // then
        Post afterPost = postRepository.findById(1L).get();
        assertThat(afterPost.getContent()).isEqualTo(content);
    }
    @DisplayName("게시글에 권한이 없는 유저가 게시글을 수정할 경우 예외가 발생한다.")
    @Test
    void modifyPost_fail() {

        // given
        String content = "수정 후 내용";
        Post post = postRepository.findById(1L).get();

        PostModifyRequest postModifyRequest = PostModifyRequestBuilder.build(content);

        // when, then
        assertThatThrownBy(() -> postService.modifyPost(post.getId(), postModifyRequest, 2L))
                .isInstanceOf(PostAccessDeniedException.class);
    }

    @DisplayName("게시글 삭제 성공")
    @Test
    void deletePost_success() {

        // given
        Post beforePost = postRepository.findById(1L).get();  // status : NORMAL
        Member member = memberRepository.findById(1L).get();

        // when
        postService.deletePost(beforePost.getId(), member.getId());

        // then
        Post afterPost = postRepository.findById(1L).get(); // status : DELETE
        assertThat(afterPost.getState()).isEqualTo(State.DELETE);
    }

    @DisplayName("게시글에 권한이 없는 유저가 게시글을 삭제할 경우 예외가 발생한다")
    @Test
    void deletePost_fail() {

        // given
        Post post = postRepository.findById(1L).get();  // status : NORMAL

        // when, then
        assertThatThrownBy(() -> postService.deletePost(post.getId(), 2L))
                .isInstanceOf(PostAccessDeniedException.class);
    }
}
