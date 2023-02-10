package com.masil.domain.comment.repository;

import com.masil.domain.comment.entity.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
//    Slice<Comment> findAllByPostIdAndParentIdNull(Long postId, Pageable pageable);
    Page<Comment> findAllByPostIdAndParentIdNull(Long postId, Pageable pageable);

    Long countByPostId(Long postId);

//    Page<Comment>

}