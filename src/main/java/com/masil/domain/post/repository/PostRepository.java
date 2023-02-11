package com.masil.domain.post.repository;

import com.masil.domain.post.entity.Post;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {

    Slice<Post> findAllByBoardIdAndEmdAddressId(Long boardId, Integer emdAddressId, Pageable pageable);

    Slice<Post> findAllByBoardIdAndEmdAddressSggAddressId(Long boardId, Integer sggAddressId, Pageable pageable);

    @Modifying
    @Query(value = "UPDATE Post p set p.viewCount = p.viewCount + 1 where p.id = :postId")
    void increaseViewCount(Long postId);
}