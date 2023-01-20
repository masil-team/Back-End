package com.masil.domain.postlike.repository;

import com.masil.domain.post.entity.Post;
import com.masil.domain.postlike.entity.PostLike;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostLikeRepository extends JpaRepository<PostLike, Long> {

    boolean existsByPostAndMemberId(Post post, Long memberId);
}
