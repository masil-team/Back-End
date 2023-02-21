package com.masil.domain.comment.repository;

import com.masil.domain.comment.entity.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    @Query(value = "select * from comment p " +
            "left join comment c on p.id = c.parent_id " +
            "where p.parent_id is null AND NOT (p.state = 'DELETE' AND c.id is NULL) " +
            "and p.post_id = :postId " +
            "GROUP BY p.id, c.parent_id " +
            "Order By p.id DESC ",
            countQuery = "SELECT * from comment c where c.parent_id is null",
            nativeQuery = true)
    Page<Comment> findAllByPostId(@Param("postId")Long postId, Pageable pageable);

    Long countByPostId(Long postId);
}