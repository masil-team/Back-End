package com.masil.domain.member.service;

import com.masil.common.annotation.ServiceTest;
import com.masil.domain.address.entity.EmdAddress;
import com.masil.domain.address.repository.EmdAddressRepository;
import com.masil.domain.board.repository.BoardRepository;
import com.masil.domain.comment.entity.Comment;
import com.masil.domain.comment.repository.CommentRepository;
import com.masil.domain.commentlike.repository.CommentLikeRepository;
import com.masil.domain.fixture.BoardFixture;
import com.masil.domain.member.dto.request.MyFindRequest;
import com.masil.domain.member.entity.Member;
import com.masil.domain.member.repository.MemberRepository;
import com.masil.domain.post.dto.PostsResponse;
import com.masil.domain.post.entity.Post;
import com.masil.domain.post.entity.State;
import com.masil.domain.post.repository.PostRepository;
import com.masil.domain.post.service.PostService;
import com.masil.domain.postlike.repository.PostLikeRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;

import static com.masil.domain.fixture.PostFixture.일반_게시글_JJ;
import static org.springframework.data.domain.Sort.Direction.DESC;

public class MyServiceTest extends ServiceTest {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private BoardRepository boardRepository;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private PostLikeRepository postLikeRepository;

    @Autowired
    private CommentLikeRepository commentLikeRepository;

    @Autowired
    private EmdAddressRepository emdAddressRepository;

    @Autowired
    private MyService myService;

    @Autowired
    private PostService postService;

    private Member JJ;
    private Post post1;
    private Post post2;
    private Post post3;

    private Comment comment1;
    private Comment comment2;
    private Comment comment3;


    @BeforeEach
    void setUp() {
        EmdAddress emdAddress = emdAddressRepository.findById(11110111).get();

        post1 = 일반_게시글_JJ.엔티티_생성(emdAddress);
        post2 = 일반_게시글_JJ.엔티티_생성(emdAddress);
        post3 = 일반_게시글_JJ.엔티티_생성(emdAddress);

        JJ = memberRepository.save(post1.getMember());
        boardRepository.save(post1.getBoard());

        postRepository.save(post1);
        postRepository.save(post2);
        postRepository.save(post3);

        comment1 = Comment.builder()
                .content("테스트1")
                .post(post1)
                .state(State.NORMAL)
                .member(JJ)
                .build();

        comment2 = Comment.builder()
                .content("테스트2")
                .post(post1)
                .state(State.NORMAL)
                .member(JJ)
                .build();

        comment3 = Comment.builder()
                .content("테스트3")
                .post(post1)
                .state(State.NORMAL)
                .member(JJ)
                .build();

        commentRepository.save(comment1);
        commentRepository.save(comment2);
        commentRepository.save(comment3);

    }
    @Test
    @DisplayName("내가 쓴글 조회 - 성공")
    void findMyPostsSuccess() throws Exception {

        MyFindRequest request = MyFindRequest.builder()
                .memberId(JJ.getId())
                .build();

        PostsResponse myPosts = postService.findPostsByMember(request, PageRequest.of(0, 8, DESC, "createDate"));
        Assertions.assertEquals(3, myPosts.getPosts().size());
        Assertions.assertEquals(post1.getContent() , myPosts.getPosts().get(0).getContent());
    }

    @Test
    @DisplayName("내가 쓴글 조회 - 실패")
    void findMyPostsFail() throws Exception {

    }

    @Test
    @DisplayName("내가 쓴 댓글 조회 - 성공")
    void findMyCommentsSuccess() throws Exception {

    }

    @Test
    @DisplayName("내가 쓴 댓글 조회 - 실패")
    void findMyCommentsFail() throws Exception {

    }


}
