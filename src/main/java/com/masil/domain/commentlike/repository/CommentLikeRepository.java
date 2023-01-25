package com.masil.domain.commentlike.repository;

import com.masil.domain.comment.entity.Comment;
import com.masil.domain.commentlike.entity.CommentLike;
import com.masil.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CommentLikeRepository extends JpaRepository<CommentLike, Long> {

    Optional<CommentLike> findByMemberAndComment(Member member, Comment comment);
}
