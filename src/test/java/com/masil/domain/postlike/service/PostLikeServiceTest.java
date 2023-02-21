package com.masil.domain.postlike.service;

import com.masil.common.annotation.ServiceTest;
import com.masil.domain.member.entity.Member;
import com.masil.domain.member.repository.MemberRepository;
import com.masil.domain.post.entity.Post;
import com.masil.domain.post.repository.PostRepository;
import com.masil.domain.postlike.dto.PostLikeResponse;
import com.masil.domain.postlike.entity.PostLike;
import com.masil.domain.postlike.exception.SelfPostLikeException;
import com.masil.domain.postlike.repository.PostLikeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static com.masil.domain.fixture.PostFixture.일반_게시글_JJ;
import static com.masil.domain.fixture.PostFixture.일반_게시글_KK;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

class PostLikeServiceTest extends ServiceTest {

    @Autowired
    private PostLikeService postLikeService;
    @Autowired
    private PostLikeRepository postLikeRepository;
    @Autowired
    public MemberRepository memberRepository;
    @Autowired
    public PostRepository postRepository;

    private Post post1;
    private Post post2;
    private Member JJ;
    private Member KK;

    @BeforeEach
    void setUp() {
        post1 = 일반_게시글_JJ.엔티티_생성();
        JJ = memberRepository.save(post1.getMember());
        postRepository.save(post1);

        post2 = 일반_게시글_KK.엔티티_생성();
        KK = memberRepository.save(post2.getMember());
        postRepository.save(post2);
    }

    @DisplayName("KK가 게시글1에 좋아요를 누른다.")
    @Test
    void toggleLikePost_plusLike() {

        // when
        PostLikeResponse postLikeResponse = postLikeService.toggleLikePost(post1.getId(), KK.getId());

        // then
        PostLike postLike = postLikeRepository.findByPostAndMember(post1, KK).get();
        Post likedPost = postRepository.findById(post1.getId()).get();

        assertAll(
                () -> assertThat(postLikeResponse.getLikeCount()).isEqualTo(1),
                () -> assertThat(postLikeResponse.getIsLike()).isTrue(),
                () -> assertThat(postLike.getPost()).isEqualTo(post1),
                () -> assertThat(postLike.getMember()).isEqualTo(KK),
                () -> assertThat(likedPost.getLikeCount()).isEqualTo(1)
        );
    }

    @DisplayName("KK가 게시글1에 좋아요를 취소한다.")
    @Test
    void toggleLikePost_minusLike() {
        // then
        PostLikeResponse postLikeResponse = postLikeService.toggleLikePost(post1.getId(), KK.getId());

        // when
        PostLikeResponse canceledPostLikeResponse = postLikeService.toggleLikePost(post1.getId(), KK.getId());

        // then
        PostLike postLike = postLikeRepository.findByPostAndMember(post1, KK).orElse(null);
        Post canceledPost = postRepository.findById(post1.getId()).get();

        assertAll(
                () -> assertThat(postLike).isNull(),
                () -> assertThat(canceledPostLikeResponse.getLikeCount()).isEqualTo(postLikeResponse.getLikeCount()-1),
                () -> assertThat(canceledPostLikeResponse.getIsLike()).isFalse(),
                () -> assertThat(canceledPost.getLikeCount()).isEqualTo(0)
        );
    }

    @DisplayName("본인 글에 좋아요를 누를 경우 예외가 발생한다.")
    @Test
    void toggleLikePost_isOwner() {

        // then, when
        assertThatThrownBy(() -> postLikeService.toggleLikePost(post1.getId(), JJ.getId()))
                .isInstanceOf(SelfPostLikeException.class);
    }

}