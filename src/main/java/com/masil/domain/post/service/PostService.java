package com.masil.domain.post.service;

import com.masil.domain.post.dto.*;

import com.masil.domain.post.entity.Post;
import com.masil.domain.post.exception.PostNotFoundException;
import com.masil.domain.post.repository.PostRepository;
import com.masil.domain.member.entity.Member;
import com.masil.domain.member.repository.MemberRepository;

import com.masil.domain.postlike.repository.PostLikeRepository;
import com.masil.global.error.exception.BusinessException;
import com.masil.global.error.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PostService {

    private final PostRepository postRepository;
    private final MemberRepository memberRepository;
    private final PostLikeRepository postLikeRepository;

    public PostDetailResponse findDetailPost(Long postId, Long memberId) { // memberId 임시값
        Post post = findPostById(postId);
        post.plusView();

        // 본인 글인지 체크
        boolean isOwner = post.isOwner(memberId);

        // 좋아요한 글인지 체크
        boolean isLike = postLikeRepository.existsByPostAndMemberId(post, memberId);

        return PostDetailResponse.of(post, isOwner, isLike);
    }

    public PostsResponse findAllPost(Long boardId, Pageable pageable) {
        Slice<Post> posts = postRepository.findAll(pageable);
        return PostsResponse.ofPosts(posts);
    }

    @Transactional
    public Long createPost(PostCreateRequest postCreateRequest, Long memberId){
        Member member = findUserById(memberId);
        Post post = postCreateRequest.toEntity(member);
        return postRepository.save(post).getId();
    }

    @Transactional
    public void modifyPost(Long postId, PostModifyRequest postModifyRequest, Long memberId){
        Post post = findPostById(postId);
        findUserById(memberId);

        validateOwner(memberId, post);

        post.updateContent(postModifyRequest.getContent());
    }

    @Transactional
    public void deletePost(Long postId, Long memberId) {
        Post post = findPostById(postId);
        findUserById(memberId);

        validateOwner(memberId, post);

        post.tempDelete();
    }

    private void validateOwner(Long memberId, Post post) {
        if (!post.isOwner(memberId)) {
            throw new BusinessException(ErrorCode.HANDLE_ACCESS_DENIED); // 추후 변경
        }
    }

    private Post findPostById(Long postId) {
        return postRepository.findById(postId)
                .orElseThrow(PostNotFoundException::new);
    }

    private Member findUserById(Long memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(RuntimeException::new);
    }
}
