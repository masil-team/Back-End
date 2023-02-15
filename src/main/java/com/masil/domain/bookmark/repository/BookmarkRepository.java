package com.masil.domain.bookmark.repository;

import com.masil.domain.bookmark.entity.Bookmark;
import com.masil.domain.member.entity.Member;
import com.masil.domain.post.entity.Post;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BookmarkRepository extends JpaRepository<Bookmark, Long> {
    boolean existsByPostAndMember(Post post, Member member);

    boolean existsByPostAndMemberId(Post post, Long memberId);

    Optional<Bookmark> findByPostAndMember(Post post, Member member);

    @EntityGraph(attributePaths = {"post"}) // fetch 조인 후 조회
    Slice<Bookmark> findAllByMember(Member member, Pageable pageable);

}
