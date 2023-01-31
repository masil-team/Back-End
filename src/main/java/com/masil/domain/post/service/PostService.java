package com.masil.domain.post.service;

import com.masil.domain.board.entity.Board;
import com.masil.domain.board.exception.BoardNotFoundException;
import com.masil.domain.board.repository.BoardRepository;
import com.masil.domain.member.exception.MemberNotFoundException;
import com.masil.domain.post.dto.*;

import com.masil.domain.post.entity.Post;
import com.masil.domain.post.exception.PostAccessDeniedException;
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

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PostService {

    private final PostRepository postRepository;
    private final MemberRepository memberRepository;
    private final PostLikeRepository postLikeRepository;
    private final BoardRepository boardRepository;

    public PostDetailResponse findDetailPost(Long postId, Long memberId) { // memberId 임시값
        Post post = findPostById(postId);
        post.plusView();

        if (memberId != -1) {
            return PostDetailResponse.of(post,
                    post.isOwner(memberId), // 본인 글인지 체크
                    postLikeRepository.existsByPostAndMemberId(post, memberId) // 좋아요한 글인지 체크
            );
        }
        return PostDetailResponse.of(post);
    }

    public PostsResponse findAllPost(Long boardId, Pageable pageable) {
        Slice<Post> posts = postRepository.findAllByBoardId(boardId, pageable);
        return PostsResponse.ofPosts(posts);
    }

    @Transactional
    public Long createPost(PostCreateRequest postCreateRequest, Long memberId){
        Member member = findMemberById(memberId);
        Board board = findBoardById(postCreateRequest.getBoardId());
        Post post = postCreateRequest.toEntity(member, board);
        return postRepository.save(post).getId();
    }

    @Transactional
    public void modifyPost(Long postId, PostModifyRequest postModifyRequest, Long memberId){
        Post post = findPostById(postId);
        findMemberById(memberId);

        validateOwner(memberId, post);

        post.updateContent(postModifyRequest.getContent());
    }

    @Transactional
    public void deletePost(Long postId, Long memberId) {
        Post post = findPostById(postId);
        findMemberById(memberId);

        validateOwner(memberId, post);

        post.tempDelete();
    }

    private void validateOwner(Long memberId, Post post) {
        if (!post.isOwner(memberId)) {
            throw new PostAccessDeniedException();
        }
    }

    private Post findPostById(Long postId) {
        return postRepository.findById(postId)
                .orElseThrow(PostNotFoundException::new);
    }

    private Member findMemberById(Long memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(MemberNotFoundException::new);
    }
    private Board findBoardById(Long boardId) {
        return boardRepository.findById(boardId)
                .orElseThrow(BoardNotFoundException::new);
    }
}
