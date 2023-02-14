package com.masil.domain.comment.repository;

import com.masil.domain.comment.entity.Comment;
import org.hibernate.sql.Select;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import javax.validation.Valid;
import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    @Query(value = "select * from comment p " +
            "left join comment c on p.id = c.parent_id " +
            "where p.parent_id is null AND NOT (p.state = \"DELETE\" AND c.id is NULL) GROUP BY p.id, c.parent_id",
            countQuery = "SELECT * from Comment c where c.parent_id is null",
            nativeQuery = true)
    Page<Comment> findAllByPostId(Long postId, Pageable pageable);
//    Page<Comment> findAllByPostIdAndParentIdNull(Long postId, Pageable pageable);

    Long countByPostId(Long postId);
}