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

//    String SEARCH_SQL = "SELECT * from post where "
//            + "(:query is null or :query = '') "
//            + "or "
//            + "(content regexp :query) ";

    Slice<Post> findAllByEmdAddressId(Integer rCode, Pageable pageable);

    Slice<Post> findAllByBoardIdAndEmdAddressId(Long boardId, Integer emdAddressId, Pageable pageable);

    Slice<Post> findAllByEmdAddressSggAddressId(Integer rCode, Pageable pageable);

    Slice<Post> findAllByBoardIdAndEmdAddressSggAddressId(Long boardId, Integer sggAddressId, Pageable pageable);

//    @Query(value = "select * from Post p Where p.content LIKE %:keyword%",
//            countQuery = "select * from Post p ",
//            nativeQuery = true)
    Slice<Post> findByContentContainingIgnoreCaseAndEmdAddressId(String keyword, Integer emdAddressId, Pageable pageable);

    @Modifying
    @Query(value = "UPDATE Post p set p.viewCount = p.viewCount + 1 where p.id = :postId")
    void increaseViewCount(Long postId);

    Slice<Post> findAllByMemberId(Long memberId, Pageable pageable);
}