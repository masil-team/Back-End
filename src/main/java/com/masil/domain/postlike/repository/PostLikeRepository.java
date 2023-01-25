package com.masil.domain.postlike.repository;

import com.masil.domain.member.entity.Member;
import com.masil.domain.post.entity.Post;
import com.masil.domain.postlike.entity.PostLike;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PostLikeRepository extends JpaRepository<PostLike, Long> {

    boolean existsByPostAndMemberId(Post post, Long memberId);

    Optional<PostLike> findByPostAndMember(Post post, Member member);
}
