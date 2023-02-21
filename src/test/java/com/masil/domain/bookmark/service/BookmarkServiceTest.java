package com.masil.domain.bookmark.service;

import com.masil.common.annotation.ServiceTest;
import com.masil.domain.bookmark.dto.BookmarkResponse;
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

import static com.masil.domain.fixture.PostFixture.일반_게시글_JJ;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;


class BookmarkServiceTest extends ServiceTest {

    @Autowired
    private BookmarkService bookmarkService;
    @Autowired
    private BookmarkRepository bookmarkRepository;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private PostRepository postRepository;

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

}