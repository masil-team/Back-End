package com.masil.domain.commentlike.repository;

import com.masil.domain.comment.entity.Comment;
import com.masil.domain.commentlike.entity.CommentLike;
import com.masil.domain.member.entity.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CommentLikeRepository extends JpaRepository<CommentLike, Long> {

    Optional<CommentLike> findByMemberAndComment(Member member, Comment comment);

    boolean existsByCommentAndMemberId(Comment comment, Long memberId);

    Page<CommentLike> findAllByMemberId(Long memberId, Pageable pageable);
}
