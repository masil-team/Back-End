package com.masil.domain.post.service;

import com.masil.domain.board.entity.Board;
import com.masil.domain.board.exception.BoardNotFoundException;
import com.masil.domain.board.repository.BoardRepository;
import com.masil.domain.bookmark.repository.BookmarkRepository;
import com.masil.domain.member.dto.request.MyFindRequest;
import com.masil.domain.member.entity.Member;
import com.masil.domain.member.exception.MemberNotFoundException;
import com.masil.domain.member.repository.MemberRepository;
import com.masil.domain.post.dto.*;
import com.masil.domain.post.entity.Post;
import com.masil.domain.post.entity.SearchQuery;
import com.masil.domain.post.exception.PostAccessDeniedException;
import com.masil.domain.post.exception.PostNotFoundException;
import com.masil.domain.post.exception.PostSearchInputException;
import com.masil.domain.post.repository.PostRepository;
import com.masil.domain.postFile.entity.PostFile;
import com.masil.domain.postFile.repository.PostFileRepository;
import com.masil.domain.postlike.repository.PostLikeRepository;
import com.masil.domain.storage.exception.InvalidImageFileException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import javax.annotation.Nullable;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PostService {

    private static final int MIN_LENGTH = 2;
    private static final int ALL_CATEGORY = 1;

    private final PostRepository postRepository;
    private final MemberRepository memberRepository;
    private final PostLikeRepository postLikeRepository;
    private final BoardRepository boardRepository;
    private final BookmarkRepository bookmarkRepository;
    private final PostFileRepository postFileRepository;

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

        List<PostFile> postFiles = postFileRepository.findByIdIn(postCreateRequest.getFileIds());

        for (PostFile postFile : postFiles) {
            // 이미 이미지의 주인이 있는 경우 예외 반환
            if (postFile.getPost() != null)
                throw new InvalidImageFileException();
            post.addPostFiles(postFile);
        }

        return postRepository.save(post).getId();
    }

    @Transactional
    public void modifyPost(Long postId, PostModifyRequest postModifyRequest, Long memberId) {
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

    /**
     * 검색 쿼리 추가
     */
    public PostsResponse searchPosts(@Nullable String keyword, Integer rCode, Pageable pageable){
        validateLength(keyword);
        pageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.Direction.DESC, "createDate");
        SearchQuery searchQuery = new SearchQuery(keyword);
        Slice<Post> posts = postRepository.findByContentContainingIgnoreCaseAndEmdAddressId(searchQuery.getValue(), rCode, pageable);

        return PostsResponse.ofPosts(posts);
    }

    private Slice<Post> findMatchingPosts(PostFilterRequest postFilterRequest) {
        if (postFilterRequest.isEmdAddress()) {
            return findAllPostByEmdId(postFilterRequest);
        } else {
            return findAllPostBySggId(postFilterRequest);
        }
    }

    private Slice<Post> findAllPostBySggId(PostFilterRequest request) {
        if (request.getBoardId() == ALL_CATEGORY) {
            return postRepository.findAllByEmdAddressSggAddressId(request.getRCode(), request.getPageable());
        }
        return postRepository.findAllByBoardIdAndEmdAddressSggAddressId(request.getBoardId(), request.getRCode(), request.getPageable());
    }

    private Slice<Post> findAllPostByEmdId(PostFilterRequest request) {
        if (request.getBoardId() == ALL_CATEGORY) {
            return postRepository.findAllByEmdAddressId(request.getRCode(), request.getPageable());
        }
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

    /**
     * 글자 수 초과 exception
     */
    public void validateLength(String content) {
        if (content.length() < MIN_LENGTH) {
            throw new PostSearchInputException();
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

    public PostsResponse findPostsByMember(MyFindRequest request, Pageable pageable) {
        Slice<Post> myPosts = postRepository.findAllByMemberId(request.getMemberId(), pageable);
        myPosts.forEach(myPost
                -> updatePostPermissionsForMember(request.getMemberId(), myPost));
        return PostsResponse.ofPosts(myPosts);
    }
}
