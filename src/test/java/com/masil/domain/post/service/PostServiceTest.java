package com.masil.domain.post.service;

import com.masil.common.annotation.ServiceTest;
import com.masil.domain.address.entity.EmdAddress;
import com.masil.domain.address.repository.EmdAddressRepository;
import com.masil.domain.board.entity.Board;
import com.masil.domain.board.repository.BoardRepository;
import com.masil.domain.bookmark.service.BookmarkService;
import com.masil.domain.member.entity.Member;
import com.masil.domain.member.repository.MemberRepository;
import com.masil.domain.post.dto.*;
import com.masil.domain.post.entity.Post;
import com.masil.domain.post.entity.State;
import com.masil.domain.post.exception.PostAccessDeniedException;
import com.masil.domain.post.exception.PostNotFoundException;
import com.masil.domain.post.repository.PostRepository;
import com.masil.domain.postlike.service.PostLikeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

import static com.masil.domain.fixture.BoardFixture.*;
import static com.masil.domain.fixture.MemberFixture.*;
import static com.masil.domain.fixture.PostFixture.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

public class PostServiceTest extends ServiceTest {

    @Autowired
    private PostService postService;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private PostRepository postRepository;
    @Autowired
    private BoardRepository boardRepository;
    @Autowired
    private PostLikeService postLikeService;
    @Autowired
    private EmdAddressRepository emdAddressRepository; // 임시
    @Autowired
    private BookmarkService bookmarkService;

    @PersistenceContext
    private EntityManager em;

    private static final Long NOT_FOUND_POST_ID = 9999L;

    @Nested
    @DisplayName("게시글 상세 조회를 할 때")
    class FindDetailPost {

        private Post post;

        @BeforeEach
        void setUp() {
            // TODO : emd 추후 제거
            EmdAddress emdAddress = emdAddressRepository.findById(11110111).get();

            post = 일반_게시글_JJ.엔티티_생성(emdAddress);
            memberRepository.save(post.getMember());
            boardRepository.save(post.getBoard());
            postRepository.save(post);
        }

        @Test
        @DisplayName("성공적으로 조회한다.")
        void success() {

            // when
            PostDetailResponse postDetailResponse = postService.findDetailPost(post.getId(), post.getMember().getId());

            // then
            assertAll(
                    () -> assertThat(postDetailResponse.getId()).isEqualTo(1L),
                    () -> assertThat(postDetailResponse.getMember().getId()).isEqualTo(post.getMember().getId()),
                    () -> assertThat(postDetailResponse.getBoardId()).isEqualTo(post.getBoard().getId()),
                    () -> assertThat(postDetailResponse.getContent()).isEqualTo(post.getContent()),
                    () -> assertThat(postDetailResponse.getAddress()).isEqualTo(post.getEmdAddress().getEmdName()), // 추후 수정,
                    () -> assertThat(postDetailResponse.getIsOwner()).isEqualTo(true),
                    () -> assertThat(postDetailResponse.getIsLiked()).isEqualTo(false),
                    () -> assertThat(postDetailResponse.getIsScrap()).isEqualTo(false)
            );
        }

        @Test
        @DisplayName("다른 사람의 글을 조회한다.")
        void not_isOwner() {

            // given
            Member KK = memberRepository.save(일반_회원_KK.엔티티_생성());

            // when
            PostDetailResponse postDetailResponse = postService.findDetailPost(post.getId(), KK.getId());

            // then
            assertThat(postDetailResponse.getIsOwner()).isEqualTo(false);
        }

        @Test
        @DisplayName("조회수가 1 증가한다.")
        void view_count() {

            //given
            em.clear();

            // when
            PostDetailResponse postDetailResponse = postService.findDetailPost(post.getId(), post.getMember().getId());

            // then
            assertThat(postDetailResponse.getViewCount()).isEqualTo(1);
        }

        @DisplayName("좋아요한 게시글을 조회한다.")
        @Test
        void findPost_isLike() {
            // given
            Member KK = memberRepository.save(일반_회원_KK.엔티티_생성());
            postLikeService.toggleLikePost(post.getId(), KK.getId());

            // when
            PostDetailResponse postDetailResponse = postService.findDetailPost(post.getId(), KK.getId());

            // then
            assertThat(postDetailResponse.getIsLiked()).isEqualTo(true);
        }

        @DisplayName("즐겨찾기한 게시글을 조회한다.")
        @Test
        void findPost_isScrap() {
            // given
            bookmarkService.addBookmark(post.getId(), post.getMember().getId());

            // when
            PostDetailResponse postDetailResponse = postService.findDetailPost(post.getId(),  post.getMember().getId());

            // then
            assertThat(postDetailResponse.getIsScrap()).isEqualTo(true);
        }

        @Test
        @DisplayName("존재하지 않는 게시글일 경우 예외가 발생한다")
        void not_found() {

            // when, then
            assertThatThrownBy(() -> postService.findDetailPost(NOT_FOUND_POST_ID, post.getMember().getId()))
                    .isInstanceOf(PostNotFoundException.class);
        }

        @Test
        @DisplayName("삭제 상태인 게시글인 경우 예외가 발생한다")
        void findPost_isDeleted() {

            // given
            post.tempDelete();

            // when, then
            assertThatThrownBy(() -> postService.findDetailPost(post.getId(), post.getMember().getId()))
                    .isInstanceOf(PostNotFoundException.class);
        }
    }

    @Nested
    @DisplayName("게시글 목록 조회를 할 때")
    class FindPosts {

        private List<Post> posts;
        private Post post;

        @BeforeEach
        void setUp() {
            // TODO : emd 추후 제거
            EmdAddress emdAddress = emdAddressRepository.findById(11110111).get();
            posts = 일반_게시글_JJ.엔티티_여러개_생성(emdAddress);
            post = posts.get(0);

            memberRepository.save(post.getMember());
            boardRepository.save(post.getBoard());
            postRepository.saveAll(posts);
        }
        @Test
        @DisplayName("성공적으로 조회한다.")
        void success() {
            // given
            PostFilterRequest postFilterRequest = PostFilterRequestBuilder.build();

            // when
            PostsResponse postsResponse = postService.findPosts(postFilterRequest, post.getMember().getId());
            List<PostsElementResponse> postsElementResponseList = postsResponse.getPosts();
            PostsElementResponse postsElementResponse = postsElementResponseList.get(0);

            // then
            assertAll(
                    () -> assertThat(postsElementResponseList.size()).isEqualTo(posts.size()),
                    () -> assertThat(postsElementResponse.getId()).isEqualTo(2L),
                    () -> assertThat(postsResponse.getIsLast()).isTrue()
            );
        }

        @DisplayName("상태가 DELETE 인 게시글은 제외하고 조회한다.")
        @Test
        void findPosts_isDeleted() {

            // given
            Post foundPost = postRepository.findById(1L).get();
            foundPost.tempDelete();
            PostFilterRequest postFilterRequest = PostFilterRequestBuilder.build();

            // when
            PostsResponse foundPosts = postService.findPosts(postFilterRequest, post.getMember().getId());
            List<PostsElementResponse> postList = foundPosts.getPosts();

            // then
            assertThat(postList.get(0).getId()).isEqualTo(2L);
        }

    }

    @Nested
    @DisplayName("게시글 생성할 때")
    class CreatePost {

        private Member JJ;
        private Board board;

        @BeforeEach
        void setUp() {
            JJ = memberRepository.save(일반_회원_JJ.엔티티_생성());
            board = boardRepository.save(전체_카테고리.엔티티_생성());
        }

        @Test
        @DisplayName("성공적으로 생성된다.")
        void success() {

            // given
            String content = "새로운 내용";
            PostCreateRequest postCreateRequest = PostCreateRequestBuilder.build(content, board.getId());

            // when
            Long postId = postService.createPost(postCreateRequest, JJ.getId());
            Post foundPost = postRepository.findById(postId).get();

            // then
            assertAll(
                    () -> assertThat(foundPost.getId()).isEqualTo(postId),
                    () -> assertThat(foundPost.getContent()).isEqualTo(content),
                    () -> assertThat(foundPost.getMember().getId()).isEqualTo(JJ.getId()),
                    () -> assertThat(foundPost.getViewCount()).isEqualTo(0),
                    () -> assertThat(foundPost.getBoard().getId()).isEqualTo(board.getId()),
                    () -> assertThat(foundPost.getState()).isEqualTo(State.NORMAL)
            );

        }
    }

    @Nested
    @DisplayName("게시글 수정할 때")
    class ModifyPost {

        private Post post;
        private Member JJ;

        @BeforeEach
        void setUp() {
            // TODO : emd 추후 제거
            EmdAddress emdAddress = emdAddressRepository.findById(11110111).get();

            post = 일반_게시글_JJ.엔티티_생성(emdAddress);
            JJ = memberRepository.save(post.getMember());
            boardRepository.save(post.getBoard());
            postRepository.save(post);
        }

        @Test
        @DisplayName("성공적으로 수정된다.")
        void modifyPost_success() {

            // given
            String content = "수정 후 내용";
            PostModifyRequest postModifyRequest = PostModifyRequestBuilder.build(content);

            // when
            postService.modifyPost(post.getId(), postModifyRequest, JJ.getId());

            // then
            Post modifiedPost = postRepository.findById(post.getId()).get();
            assertThat(modifiedPost.getContent()).isEqualTo(content);
        }

        @DisplayName("게시글에 권한이 없는 유저가 수정할 경우 예외가 발생한다.")
        @Test
        void modifyPost_fail() {

            // given
            Member KK = memberRepository.save(일반_회원_KK.엔티티_생성());
            String content = "수정 후 내용";
            PostModifyRequest postModifyRequest = PostModifyRequestBuilder.build(content);

            // when, then
            assertThatThrownBy(() -> postService.modifyPost(post.getId(), postModifyRequest, KK.getId()))
                    .isInstanceOf(PostAccessDeniedException.class);
        }
    }

    @Nested
    @DisplayName("게시글 삭제할 때")
    class DeletePost {

        private Post post;
        private Member JJ;

        @BeforeEach
        void setUp() {
            // TODO : emd 추후 제거
            EmdAddress emdAddress = emdAddressRepository.findById(11110111).get();

            post = 일반_게시글_JJ.엔티티_생성(emdAddress);
            JJ = memberRepository.save(post.getMember());
            boardRepository.save(post.getBoard());
            postRepository.save(post);
        }

        @DisplayName("성공적으로 삭제된다.")
        @Test
        void deletePost_success() {

            // when
            postService.deletePost(post.getId(), JJ.getId());

            // then
            Post deletedPost = postRepository.findById(post.getId()).get(); // status : DELETE
            assertThat(deletedPost.getState()).isEqualTo(State.DELETE);
        }

        @DisplayName("게시글에 권한이 없는 유저가 삭제할 경우 예외가 발생한다")
        @Test
        void deletePost_fail() {

            // given
            Member KK = memberRepository.save(일반_회원_KK.엔티티_생성());

            // when, then
            assertThatThrownBy(() -> postService.deletePost(post.getId(), KK.getId()))
                    .isInstanceOf(PostAccessDeniedException.class);
        }
    }
}
