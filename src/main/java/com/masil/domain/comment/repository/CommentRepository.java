package com.masil.domain.comment.repository;

import com.masil.domain.comment.dto.CommentResponse;
import com.masil.domain.comment.entity.Comment;
import com.masil.domain.member.entity.Member;
import com.masil.domain.post.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findAllByPostId(Long postId);
}
