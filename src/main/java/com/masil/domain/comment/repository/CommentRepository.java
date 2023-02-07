package com.masil.domain.comment.repository;

import com.masil.domain.comment.entity.Comment;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findAllByPostIdAndParentIdNull(Long postId, Pageable pageable);
}