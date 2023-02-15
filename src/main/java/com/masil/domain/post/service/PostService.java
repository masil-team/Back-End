package com.masil.domain.post.service;

import com.masil.domain.board.entity.Board;
import com.masil.domain.board.exception.BoardNotFoundException;
import com.masil.domain.board.repository.BoardRepository;
import com.masil.domain.bookmark.repository.BookmarkRepository;
import com.masil.domain.member.entity.Member;
import com.masil.domain.member.exception.MemberNotFoundException;
import com.masil.domain.member.repository.MemberRepository;
import com.masil.domain.post.dto.*;
import com.masil.domain.post.entity.Post;
import com.masil.domain.post.exception.PostAccessDeniedException;
import com.masil.domain.post.exception.PostNotFoundException;
import com.masil.domain.post.repository.PostRepository;
import com.masil.domain.postlike.repository.PostLikeRepository;
import lombok.RequiredArgsConstructor;
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
    private final BookmarkRepository bookmarkRepository;

    @Transactional
    public PostDetailResponse findDetailPost(Long postId, Long memberId) {
        postRepository.increaseViewCount(postId);

        Post post = findPostById(postId);
        checkPostState(post);

        // 로그인 상태인 경우
        if (memberId != null) {
            updatePostPermissionsForMember(memberId, post);
        }

        return PostDetailResponse.of(post);
    }

    public PostsResponse findPosts(PostFilterRequest postFilterRequest, Long memberId) {
        Slice<Post> posts = findMatchingPosts(postFilterRequest);

        // 회원일 경우
        if (memberId != null) {
            posts.forEach(post -> updatePostPermissionsForMember(memberId, post));
        }

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
        Board board = findBoardById(postModifyRequest.getBoardId());

        validateOwner(memberId, post);

        post.updateContentAndBoard(postModifyRequest.getContent(), board);
    }

    @Transactional
    public void deletePost(Long postId, Long memberId) {
        Post post = findPostById(postId);
        findMemberById(memberId);

        validateOwner(memberId, post);

        post.tempDelete();
    }

    private Slice<Post> findMatchingPosts(PostFilterRequest postFilterRequest) {
        if (postFilterRequest.isEmdAddress()) {
            return findAllPostByEmdId(postFilterRequest);
        } else {
            return findAllPostBySggId(postFilterRequest);
        }
    }

    private Slice<Post> findAllPostBySggId(PostFilterRequest request) {
        return postRepository.findAllByBoardIdAndEmdAddressSggAddressId(request.getBoardId(), request.getRCode(), request.getPageable());
    }

    private Slice<Post> findAllPostByEmdId(PostFilterRequest request) {
        return postRepository.findAllByBoardIdAndEmdAddressId(request.getBoardId(), request.getRCode(), request.getPageable());
    }

    private void updatePostPermissionsForMember(Long memberId, Post post) {
        boolean isOwnPost = post.isOwner(memberId);
        boolean isLiked = postLikeRepository.existsByPostAndMemberId(post, memberId);
        boolean isScrap = bookmarkRepository.existsByPostAndMemberId(post, memberId);
        post.updatePostPermissions(isOwnPost, isLiked, isScrap);
    }

    private void checkPostState(Post post) {
        if (post.isDeleted()) {
            throw new PostNotFoundException();
        }
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
