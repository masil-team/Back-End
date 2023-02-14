package com.masil.domain.bookmark.service;

import com.masil.common.annotation.ServiceTest;
import com.masil.domain.address.entity.EmdAddress;
import com.masil.domain.address.repository.EmdAddressRepository;
import com.masil.domain.board.repository.BoardRepository;
import com.masil.domain.bookmark.dto.BookmarkResponse;
import com.masil.domain.bookmark.dto.BookmarksElementResponse;
import com.masil.domain.bookmark.dto.BookmarksResponse;
import com.masil.domain.bookmark.entity.Bookmark;
import com.masil.domain.bookmark.exception.BookmarkAlreadyExistsException;
import com.masil.domain.bookmark.exception.BookmarkNotFoundException;
import com.masil.domain.bookmark.repository.BookmarkRepository;
import com.masil.domain.member.entity.Member;
import com.masil.domain.member.repository.MemberRepository;
import com.masil.domain.post.entity.Post;
import com.masil.domain.post.repository.PostRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;


import java.util.List;

import static com.masil.domain.fixture.PostFixture.일반_게시글_JJ;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.springframework.data.domain.Sort.Direction.DESC;


class BookmarkServiceTest extends ServiceTest {

    @Autowired
    private BookmarkService bookmarkService;
    @Autowired
    private BookmarkRepository bookmarkRepository;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private PostRepository postRepository;
    @Autowired
    private EmdAddressRepository emdAddressRepository;
    @Autowired
    private BoardRepository boardRepository;

    private Member JJ;
    private Post post1;
    @BeforeEach
    void setUp() {
        post1 = 일반_게시글_JJ.엔티티_생성();
        JJ = memberRepository.save(post1.getMember());
        postRepository.save(post1);
    }
    @Test
    @DisplayName("북마크를 성공적으로 추가한다")
    void addBookmark_success() {

        // when
        BookmarkResponse bookmarkResponse = bookmarkService.addBookmark(post1.getId(), JJ.getId());

        // then
        Bookmark bookmark = bookmarkRepository.findById(1L).get();

        assertAll(
                () -> assertThat(bookmark.getPost()).isEqualTo(post1),
                () -> assertThat(bookmark.getMember()).isEqualTo(JJ),
                () ->assertThat(bookmarkResponse.getIsScrap()).isTrue()
        );
    }

    @Test
    @DisplayName("북마크를 추가하는데 이미 존재하는 경우 예외가 발생한다.")
    void addBookmark_already_exists() {

        // when
        bookmarkService.addBookmark(post1.getId(), JJ.getId());

        // then
        assertThatThrownBy(() -> bookmarkService.addBookmark(post1.getId(), JJ.getId()))
                .isInstanceOf(BookmarkAlreadyExistsException.class);
    }
    @Test
    @DisplayName("북마크를 삭제한다")
    void deleteBookmark() {

        // given
        bookmarkService.addBookmark(post1.getId(), JJ.getId());

        // when
        BookmarkResponse bookmarkResponse = bookmarkService.deleteBookmark(post1.getId(), JJ.getId());
        Bookmark bookmark = bookmarkRepository.findByPostAndMember(post1, JJ).orElse(null);

        // then
        assertThat(bookmark).isNull();
        assertThat(bookmarkResponse.getIsScrap()).isFalse();
    }

    @Test
    @DisplayName("북마크를 삭제하는데 존재하지 않을 경우 예외가 발생한다.")
    void deleteBookmark_not_found() {

        // when then
        assertThatThrownBy(() -> bookmarkService.deleteBookmark(post1.getId(), JJ.getId()))
                .isInstanceOf(BookmarkNotFoundException.class);
    }

    @Test
    @DisplayName("유저의 전체 북마크 목록을 성공적으로 조회한다.")
    void findBookmark() {

        // given TODO : emd 추후 제거
        EmdAddress emdAddress = emdAddressRepository.findById(11110111).get();
        List<Post> posts = 일반_게시글_JJ.엔티티_여러개_생성(emdAddress);
        Post post = posts.get(0);
        memberRepository.save(post.getMember());
        boardRepository.save(post.getBoard());
        postRepository.saveAll(posts);
        for (Post post1 : posts) {
            bookmarkService.addBookmark(post1.getId(), JJ.getId());
        }

        // when
        BookmarksResponse bookmarksResponse = bookmarkService.findBookmarks(JJ.getId(), PageRequest.of(0, 8, DESC, "createDate"));
        List<BookmarksElementResponse> bookmarks = bookmarksResponse.getBookmarks();

        // then
        assertAll(
                () -> assertThat(bookmarksResponse.getIsLast()).isTrue(),
                () -> assertThat(bookmarks.size()).isEqualTo(2)
        );
    }

}