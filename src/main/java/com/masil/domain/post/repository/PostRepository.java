package com.masil.domain.post.repository;

import com.masil.domain.post.entity.Post;

import com.masil.domain.post.entity.State;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {

    Slice<Post> findAllByBoardId(Long boardId, Pageable pageable);

    Slice<Post> findAllByBoardIdAndState(Long boardId, State normal, Pageable pageable);

}