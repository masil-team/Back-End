package com.masil.domain.postlike.service;

import com.masil.domain.member.entity.Member;
import com.masil.domain.member.exception.MemberNotFoundException;
import com.masil.domain.member.repository.MemberRepository;
import com.masil.domain.post.entity.Post;
import com.masil.domain.post.exception.PostNotFoundException;
import com.masil.domain.post.repository.PostRepository;
import com.masil.domain.postlike.dto.PostLikeResponse;
import com.masil.domain.postlike.entity.PostLike;
import com.masil.domain.postlike.exception.SelfPostLikeException;
import com.masil.domain.postlike.repository.PostLikeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PostLikeService {

    private final PostLikeRepository postLikeRepository;
    private final PostRepository postRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public PostLikeResponse toggleLikePost(Long postId, Long memberId) {
        Post post = findPostById(postId);
        Member member = findMemberById(memberId);

        if (post.isOwner(memberId)) {
            throw new SelfPostLikeException(); // 추후 변경
        }

        PostLike postLike = postLikeRepository.findByPostAndMember(post, member).orElse(null);

        // 이미 좋아요 한 경우 삭제
        if (postLike != null) {
            postLikeRepository.delete(postLike);
            post.minusLike();
            return PostLikeResponse.of(post.getLikeCount(), false);
        }

        // 처음 좋아요 한 경우 추가
        PostLike newPostLike = PostLike.builder()
                .post(post)
                .member(member)
                .build();
        postLikeRepository.save(newPostLike);
        post.plusLike();
        return PostLikeResponse.of(post.getLikeCount(), true);
    }
    private Post findPostById(Long postId) {
        return postRepository.findById(postId)
                .orElseThrow(PostNotFoundException::new);
    }
    private Member findMemberById(Long memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(MemberNotFoundException::new);
    }
    
}
