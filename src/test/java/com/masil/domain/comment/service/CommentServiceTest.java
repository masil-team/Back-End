package com.masil.domain.comment.service;

import com.masil.common.annotation.ServiceTest;
import com.masil.domain.comment.entity.Comment;
import com.masil.domain.comment.repository.CommentRepository;
import com.masil.domain.member.entity.Member;
import com.masil.domain.member.repository.MemberRepository;
import com.masil.domain.post.entity.Post;
import com.masil.domain.post.entity.State;
import com.masil.domain.post.repository.PostRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.assertj.core.api.Assertions.assertThat;

//@ExtendWith(MockitoExtension.class)
class CommentServiceTest extends ServiceTest{

    @InjectMocks
    private CommentService commentService;

    @Mock
    public MemberRepository memberRepository;

    @Mock
    public CommentRepository commentRepository;

    @Mock
    public PostRepository postRepository;
//
//    private static final String POST_CONTENT_1 = "내용1";
//    private static final String POST_CONTENT_2 = "내용2";
//    private static final String COMMENT_CONTENT_1 = "댓글1";
//    private static final String COMMENT_CONTENT_2 = "댓글2";
//    private static final String USER_EMAIL = "email@naver.com";
//    private static final String USER_NICKNAME = "test123";
//
//    @BeforeEach
//    void setUp() {
//        Member member = Member.builder()
//                .email(USER_EMAIL)
//                .nickname(USER_NICKNAME)
//                .build();
//        memberRepository.save(member);
//
//        Post post1 = Post.builder()
//                .content(POST_CONTENT_1)
//                .member(member)
//                .build();
//
//        Post post2 = Post.builder()
//                .content(POST_CONTENT_2)
//                .member(member)
//                .build();
//
//        postRepository.save(post1);
//        postRepository.save(post2);
//
//        Comment comment1 = Comment.builder()
//                .post(post1)
//                .content(COMMENT_CONTENT_1)
//                .member(member)
//                .build();
//
//        Comment comment2 = Comment.builder()
//                .post(post1)
//                .content(COMMENT_CONTENT_2)
//                .member(member)
//                .build();
//
//        Comment comment3 = Comment.builder()
//                .post(post2)
//                .content(COMMENT_CONTENT_1)
//                .member(member)
//                .build();
//
//        Comment comment4 = Comment.builder()
//                .post(post2)
//                .content(COMMENT_CONTENT_2)
//                .member(member)
//                .build();
//
//        commentRepository.save(comment1);
//        commentRepository.save(comment2);
//        commentRepository.save(comment3);
//        commentRepository.save(comment4);
//    }



    @Test
    @DisplayName("댓글을 성공적으로 생성한다.")
    void createComment() {
        //given
        Member member = memberRepository.findById(1L).get();
        Post post = postRepository.findById(1L).get();

        String content = "새로운 댓글 3번쨰 댓글입니다.";
//        CommentCreateRequest commentCreateRequest = new CommentCreateRequest(content);

        // when
//        Long commentId = commentService.createComment(1L, commentCreateRequest);
//        List<CommentResponse> commentResponse = commentService.findComments(commentId);


        // then
        // List<> 로받았기 떄문에 어떻게 해결해야할지 고민해보자.

    }

    @Test
    @DisplayName("댓글을 성공적으로 수정한다.")
    void modifyComment() {
        //given

        //when

        //then
    }

    @Test
    @DisplayName("댓글을 성공적으로 조회한다.")
    void findComments() {
        //given

        //when

        //then
    }

    @DisplayName("댓글을 성공적으로 삭제한다.")
    @Test
    void deleteComment() {
        // given
        Comment beforeComment = commentRepository.findById(1L).get();  // status : NORMAL
        Post post = postRepository.findById(1L).get();

        // when
//        commentService.deleteComment(beforeComment.getId(), post.getId());

        // then
        Comment afterPost = commentRepository.findById(1L).get(); // status : DELETE
        assertThat(afterPost.getState()).isEqualTo(State.DELETE);
    }
}