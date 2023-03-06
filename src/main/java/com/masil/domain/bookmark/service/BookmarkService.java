package com.masil.domain.bookmark.service;

import com.masil.domain.bookmark.dto.BookmarkResponse;
import com.masil.domain.bookmark.entity.Bookmark;
import com.masil.domain.bookmark.exception.BookmarkAlreadyExistsException;
import com.masil.domain.bookmark.exception.BookmarkNotFoundException;
import com.masil.domain.bookmark.repository.BookmarkRepository;
import com.masil.domain.member.dto.request.MyFindRequest;
import com.masil.domain.member.entity.Member;
import com.masil.domain.member.exception.MemberNotFoundException;
import com.masil.domain.member.repository.MemberRepository;
import com.masil.domain.post.dto.PostsResponse;
import com.masil.domain.post.entity.Post;
import com.masil.domain.post.entity.State;
import com.masil.domain.post.exception.PostNotFoundException;
import com.masil.domain.post.repository.PostRepository;
import com.masil.domain.postlike.repository.PostLikeRepository;
import com.masil.global.error.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class BookmarkService {

    private final BookmarkRepository bookmarkRepository;
    private final PostRepository postRepository;
    private final MemberRepository memberRepository;
    private final PostLikeRepository postLikeRepository;

    @Transactional
    public BookmarkResponse addBookmark(Long postId, Long memberId) {
        Post post = findPostById(postId);
        Member member = findMemberById(memberId);

        validateBookmarkNotExist(post, member);

        Bookmark bookmark = Bookmark.builder()
                .post(post)
                .member(member)
                .build();
        bookmarkRepository.save(bookmark);
        return BookmarkResponse.of(true);
    }

    @Transactional
    public BookmarkResponse deleteBookmark(Long postId, Long memberId) {
        Post post = findPostById(postId);
        Member member = findMemberById(memberId);

        Bookmark bookmark = findBookmark(post, member);

        bookmarkRepository.delete(bookmark);
        return BookmarkResponse.of(false);
    }
    
    private void validateBookmarkNotExist(Post post, Member member) {
        if (bookmarkRepository.existsByPostAndMember(post, member))
            throw new BookmarkAlreadyExistsException(ErrorCode.BOOKMARK_ALREADY_EXISTS);
    }

    private Bookmark findBookmark(Post post, Member member) {
        return bookmarkRepository.findByPostAndMember(post, member)
                .orElseThrow(BookmarkNotFoundException::new);
    }
    private Post findPostById(Long postId) {
        return postRepository.findById(postId)
                .orElseThrow(PostNotFoundException::new);
    }

    private Member findMemberById(Long memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(MemberNotFoundException::new);
    }

    public PostsResponse findBookmarksByMember(MyFindRequest request, Pageable pageable) {
        Slice<Bookmark> myBookmarks = bookmarkRepository.findAllByMemberIdAndPostState(request.getMemberId(), State.NORMAL, pageable);
        for (Bookmark bookMark : myBookmarks) {
            updatePostPermissionsForMember(request.getMemberId(), bookMark.getPost());
        }
        return PostsResponse.ofBookmarks(myBookmarks);
    }

    private void updatePostPermissionsForMember(Long memberId, Post post) {
        boolean isOwnPost = post.isOwner(memberId);
        boolean isLiked = postLikeRepository.existsByPostAndMemberId(post, memberId);
        boolean isScrap = bookmarkRepository.existsByPostAndMemberId(post, memberId);
        post.updatePostPermissions(isOwnPost, isLiked, isScrap);
    }
}
