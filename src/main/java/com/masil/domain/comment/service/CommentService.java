package com.masil.domain.comment.service;

import com.masil.domain.comment.dto.CommentCreateRequest;
import com.masil.domain.comment.dto.CommentModifyRequest;
import com.masil.domain.comment.dto.CommentResponse;
import com.masil.domain.comment.entity.Comment;
import com.masil.domain.comment.exception.CommentNotFoundException;
import com.masil.domain.comment.repository.CommentRepository;
import com.masil.domain.post.dto.PostCreateRequest;
import com.masil.domain.post.dto.PostModifyRequest;
import com.masil.domain.post.dto.PostResponse;
import com.masil.domain.post.dto.PostsResponse;
import com.masil.domain.post.entity.Post;
import com.masil.domain.post.exception.PostNotFoundException;
import com.masil.domain.post.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.awt.print.Pageable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;

    /**
     * 댓글 작성
     */
    @Transactional
    public Long createComment(/*String nickname,*/ Long postId, CommentCreateRequest commentCreateRequest){
        // findByNickname 은 추후에 UserRepository 만듬
//        User user = userRepository.findByNickname(nickname);

        Post posts = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("댓글 쓰기 실패: 해당 게시글이 존재하지 않습니다. id = " +  postId));
        commentCreateRequest.setPost(posts);

        Comment comment = commentCreateRequest.toEntity();

        return commentRepository.save(comment).getId();
    }

    /**
     * 댓글 수정
     */
    @Transactional
    public void modifyComment(Long postId, CommentModifyRequest commentModifyRequest, Long commentId){
        Comment comment = findCommentById(commentId);
        findPostById(postId);

        comment.updateContent(commentModifyRequest.getContent());
    }

    /**
     * 댓글 조회 ( 추후 수정 )
     */
    public List<CommentResponse> findComments(Long postId){

        // 조회: 댓글 목록
        return commentRepository.findAllByPostId(postId)
                .stream()
                .map(comment -> CommentResponse.createCommentDto(comment))
                .collect(Collectors.toList());
    }


    /**
     * 댓글 삭제
     */
    @Transactional
    public void deleteComment(Long postId, Long commentId) {
        Comment comment = findCommentById(commentId);
        findPostById(postId);

        comment.tempDelete();
    }

    /**
     * 예외 처리
     */
    private Comment findCommentById(Long commentId) {
        return commentRepository.findById(commentId)
                .orElseThrow(CommentNotFoundException::new);
    }

    private Post findPostById(Long postId) {
        return postRepository.findById(postId)
                .orElseThrow(PostNotFoundException::new);
    }

}
