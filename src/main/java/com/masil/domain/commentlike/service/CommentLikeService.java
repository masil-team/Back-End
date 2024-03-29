package com.masil.domain.commentlike.service;

import com.masil.domain.comment.dto.CommentsResponse;
import com.masil.domain.comment.entity.Comment;
import com.masil.domain.comment.exception.CommentNotFoundException;
import com.masil.domain.comment.repository.CommentRepository;
import com.masil.domain.commentlike.dto.CommentLikeResponse;
import com.masil.domain.commentlike.entity.CommentLike;
import com.masil.domain.commentlike.exception.SelfCommentLikeException;
import com.masil.domain.commentlike.repository.CommentLikeRepository;
import com.masil.domain.member.dto.request.MyFindRequest;
import com.masil.domain.member.entity.Member;
import com.masil.domain.member.repository.MemberRepository;
import com.masil.domain.notification.dto.NotificationDto;
import com.masil.domain.notification.entity.NotificationType;
import com.masil.domain.notification.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class CommentLikeService {

    private final CommentLikeRepository commentLikeRepository;

    private final CommentRepository commentRepository;

    private final MemberRepository memberRepository;

    private final NotificationService notificationService;

    @Transactional
    public CommentLikeResponse updateLikeOfComment(Long commentId, Long memberId) {

        Comment comment = findCommentById(commentId);
        Member member = findMemberById(memberId);

        if (comment.isOwner(memberId)) {
            throw new SelfCommentLikeException();
        }

        if (!hasLikeComment(comment, member)) {
            comment.increaseLikeCount();
            return addLike(comment, member);
        }
        comment.decreaseLikeCount();
        return removeLike(comment, member);
    }

    public CommentLikeResponse addLike(Comment comment, Member member) {
        CommentLike commentLike = new CommentLike(comment, member);
        commentLikeRepository.save(commentLike); // true 처리

        notificationService.send(member, comment.getMember(), NotificationDto.of(NotificationType.COMMENT_LIKE, comment.getPost()));
        return CommentLikeResponse.of(comment.getLikeCount(), true);
    }

    public CommentLikeResponse removeLike(Comment comment, Member member) {
        CommentLike commentLike = commentLikeRepository.findByMemberAndComment(member, comment)
                .orElseThrow(() -> {throw new IllegalArgumentException("'좋아요' 기록을 찾을 수 없습니다.");
        });
        commentLikeRepository.delete(commentLike); // false 처리
        return CommentLikeResponse.of(comment.getLikeCount(), false);
    }

    public boolean hasLikeComment(Comment comment, Member member) {
        return commentLikeRepository.findByMemberAndComment(member, comment).isPresent();
    }

    private Comment findCommentById(Long commentId) {
        return commentRepository.findById(commentId)
                .orElseThrow(CommentNotFoundException::new);
    }
    private Member findMemberById(Long memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(RuntimeException::new);
    }

    public CommentsResponse findLikesByMemberId(MyFindRequest request, Pageable pageable) {
        Page<CommentLike> mylikeComments = commentLikeRepository.findAllByMemberId(request.getMemberId(), pageable);
        for (CommentLike myLikeComment : mylikeComments) {
            updateCommentPermissionsForMember(request.getMemberId(), myLikeComment.getComment());
        }
        return CommentsResponse.ofCommentLike(mylikeComments);
    }

    private void updateCommentPermissionsForMember(Long memberId, Comment comment) {
        boolean isOwner = comment.isCommentWriter(memberId);
        boolean isLiked = true;
        comment.updateIsCommentWriter(isOwner);
        comment.updateCommentLiked(isLiked);
    }
}
