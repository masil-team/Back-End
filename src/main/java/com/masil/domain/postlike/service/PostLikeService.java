package com.masil.domain.postlike.service;

import com.masil.domain.bookmark.repository.BookmarkRepository;
import com.masil.domain.member.dto.request.MyFindRequest;
import com.masil.domain.member.entity.Member;
import com.masil.domain.member.exception.MemberNotFoundException;
import com.masil.domain.member.repository.MemberRepository;
import com.masil.domain.notification.dto.NotificationDto;
import com.masil.domain.notification.entity.NotificationType;
import com.masil.domain.notification.service.NotificationService;
import com.masil.domain.post.dto.PostsResponse;
import com.masil.domain.post.entity.Post;
import com.masil.domain.post.exception.PostNotFoundException;
import com.masil.domain.post.repository.PostRepository;
import com.masil.domain.postlike.dto.PostLikeResponse;
import com.masil.domain.postlike.entity.PostLike;
import com.masil.domain.postlike.exception.SelfPostLikeException;
import com.masil.domain.postlike.repository.PostLikeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PostLikeService {

    private final PostLikeRepository postLikeRepository;
    private final PostRepository postRepository;
    private final MemberRepository memberRepository;

    private final NotificationService notificationService;

    private final BookmarkRepository bookmarkRepository;

    @Transactional
    public PostLikeResponse toggleLikePost(Long postId, Long memberId) {
        Post post = findPostById(postId);
        Member member = findMemberById(memberId);

        checkSelfPostLike(memberId, post);

        Optional<PostLike> postLikeOp = postLikeRepository.findByPostAndMember(post, member);
        return postLikeOp
                .map(postLike -> deletePostLike(post, postLike))    // 이미 좋아요 한 경우 삭제
                .orElseGet(() -> addPostLike(post, member));        // 처음 좋아요 한 경우 추가
    }

    private PostLikeResponse addPostLike(Post post, Member member) {
        PostLike newPostLike = PostLike.builder()
                .post(post)
                .member(member)
                .build();
        postLikeRepository.save(newPostLike);
        post.plusLike();
        notificationService.send(member, post.getMember(), NotificationDto.of(NotificationType.POST_LIKE, post));
        return PostLikeResponse.of(post.getLikeCount(), true);
    }

    private PostLikeResponse deletePostLike(Post post, PostLike postLike) {
        postLikeRepository.delete(postLike);
        post.minusLike();
        return PostLikeResponse.of(post.getLikeCount(), false);
    }

    private void checkSelfPostLike(Long memberId, Post post) {
        if (post.isOwner(memberId)) {
            throw new SelfPostLikeException();
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

    public PostsResponse findLikesByMemberId(MyFindRequest request, Pageable pageable) {
        Slice<PostLike> postLikes = postLikeRepository.findAllByMemberId(request.getMemberId());
        postLikes.forEach(myPostLike
                -> updatePostPermissionsForMember(request.getMemberId(), myPostLike.getPost()));
        return PostsResponse.ofPostLikes(postLikes);
    }

    private void updatePostPermissionsForMember(Long memberId, Post post) {
        boolean isOwnPost = post.isOwner(memberId);
        boolean isLiked = true;
        boolean isScrap = bookmarkRepository.existsByPostAndMemberId(post, memberId);
        post.updatePostPermissions(isOwnPost, isLiked, isScrap);
    }



}
