package com.masil.domain.postlike.service;

import com.masil.common.annotation.ServiceTest;
import com.masil.domain.member.entity.Member;
import com.masil.domain.member.repository.MemberRepository;
import com.masil.domain.post.dto.PostDetailResponse;
import com.masil.domain.post.entity.Post;
import com.masil.domain.post.exception.PostAccessDeniedException;
import com.masil.domain.post.repository.PostRepository;
import com.masil.domain.post.service.PostService;
import com.masil.domain.postlike.dto.PostLikeResponse;
import com.masil.domain.postlike.entity.PostLike;
import com.masil.domain.postlike.exception.SelfPostLikeException;
import com.masil.domain.postlike.repository.PostLikeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Commit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

class PostLikeServiceTest extends ServiceTest {

    @Autowired
    private PostLikeService postLikeService;
    @Autowired
    private PostLikeRepository postLikeRepository;
    @Autowired
    public MemberRepository memberRepository;
    @Autowired
    public PostRepository postRepository;

    private static final String POST_CONTENT_1 = "내용1";
    private static final String POST_CONTENT_2 = "내용2";
    private static final String USER_EMAIL_1 = "email1@naver.com";
    private static final String USER_EMAIL_2 = "email2@naver.com";
    private static final String USER_NICKNAME_1 = "test1";
    private static final String USER_NICKNAME_2 = "test2";
    @BeforeEach
    void setUp() {
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
                .build();
        Post post2 = Post.builder()
                .content(POST_CONTENT_2)
                .member(member1)
                .build();
        postRepository.save(post1);
        postRepository.save(post2);
    }
    @DisplayName("유저2가 게시글1에 좋아요를 누른다.")
    @Test
    void toggleLikePost_plusLike() {
        // then
        Post post = postRepository.findById(1L).get();
        Member member = memberRepository.findById(2L).get();
        int likeCount = post.getLikeCount();

        // when
        PostLikeResponse postLikeResponse = postLikeService.toggleLikePost(post.getId(), member.getId());

        // then
        PostLike postLike = postLikeRepository.findByPostAndMember(post, member).get();

        assertThat(postLikeResponse.getLikeCount()).isEqualTo(likeCount+1);
        assertThat(postLikeResponse.getIsLike()).isTrue();
        assertThat(postLike.getPost()).isEqualTo(post);
        assertThat(postLike.getMember()).isEqualTo(member);
    }

    @DisplayName("유저2가 게시글1에 좋아요를 취소한다.")
    @Test
    void toggleLikePost_minusLike() {
        // then
        Post post = postRepository.findById(1L).get();
        Member member = memberRepository.findById(2L).get();
        PostLikeResponse postLikeResponse = postLikeService.toggleLikePost(post.getId(), member.getId());

        // when
        PostLikeResponse afterPostLikeResponse = postLikeService.toggleLikePost(post.getId(), member.getId());

        // then
        PostLike postLike = postLikeRepository.findByPostAndMember(post, member).orElse(null);

        assertThat(postLike).isNull();
        assertThat(afterPostLikeResponse.getLikeCount()).isEqualTo(postLikeResponse.getLikeCount()-1);
        assertThat(afterPostLikeResponse.getIsLike()).isFalse();
    }
    @DisplayName("본인 글에 좋아요를 누를 경우 예외가 발생한다.")
    @Test
    void toggleLikePost_isOwner() {
        // then
        Post post = postRepository.findById(1L).get();
        Member member = memberRepository.findById(1L).get();

        // then, when
        assertThatThrownBy(() -> postLikeService.toggleLikePost(post.getId(), member.getId()))
                .isInstanceOf(SelfPostLikeException.class);
    }

}