package com.masil.domain.commentlike.service;

import com.masil.domain.comment.entity.Comment;
import com.masil.domain.comment.exception.CommentNotFoundException;
import com.masil.domain.comment.repository.CommentRepository;
import com.masil.domain.commentlike.entity.CommentLike;
import com.masil.domain.commentlike.exception.SelfCommentLikeException;
import com.masil.domain.commentlike.repository.CommentLikeRepository;
import com.masil.domain.member.entity.Member;
import com.masil.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class CommentLikeService {

    private final static String SUCCESS_LIKE_BOARD = "좋아요 처리 완료";
    private final static String SUCCESS_UNLIKE_BOARD = "좋아요 취소 완료";

    private final CommentLikeRepository commentLikeRepository;

    private final CommentRepository commentRepository;

    private final MemberRepository memberRepository;

    @Transactional
    public String updateLikeOfComment(Long commentId, Long memberId) {

        Comment comment = findCommentById(commentId);
        Member member = findMemberById(memberId);

        if (comment.isOwner(memberId)) {
            throw new SelfCommentLikeException(); // 추후 변경
        }

        if (!hasLikeComment(comment, member)) {
            comment.increaseLikeCount();
            return addLike(comment, member);
        }
        comment.decreaseLikeCount();
        return removeLike(comment, member);
    }

    public String addLike(Comment comment, Member member) {
        CommentLike commentLike = new CommentLike(comment, member); // true 처리
        commentLikeRepository.save(commentLike);
        return SUCCESS_LIKE_BOARD;
    }

    public String removeLike(Comment comment, Member member) {
        CommentLike commentLike = commentLikeRepository.findByMemberAndComment(member, comment)
                .orElseThrow(() -> {throw new IllegalArgumentException("'좋아요' 기록을 찾을 수 없습니다.");
        });
        commentLikeRepository.delete(commentLike);
        return SUCCESS_UNLIKE_BOARD;
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
}
