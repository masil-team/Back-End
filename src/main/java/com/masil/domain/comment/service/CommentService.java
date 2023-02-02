package com.masil.domain.comment.service;

import com.masil.domain.comment.dto.ChildrenCreateRequest;
import com.masil.domain.comment.dto.CommentCreateRequest;
import com.masil.domain.comment.dto.CommentModifyRequest;
import com.masil.domain.comment.dto.CommentResponse;
import com.masil.domain.comment.entity.Comment;
import com.masil.domain.comment.exception.CommentAccessDeniedException;
import com.masil.domain.comment.exception.CommentNotFoundException;
import com.masil.domain.comment.repository.CommentRepository;
import com.masil.domain.member.entity.Member;
import com.masil.domain.member.repository.MemberRepository;
import com.masil.domain.post.entity.Post;
import com.masil.domain.post.exception.PostNotFoundException;
import com.masil.domain.post.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final MemberRepository memberRepository;

    /**
     * 댓글 조회
     */
    public List<CommentResponse> findComments(Long postId, Pageable pageable){

        return commentRepository.findAllByPostIdAndParentIdNull(postId, pageable).stream()
                .map(comment -> CommentResponse.responseCommentDto(comment))
                .collect(Collectors.toList());
    }

    /**
     * 댓글 작성
     */
    @Transactional
    public Long createComment(Long postId, CommentCreateRequest commentCreateRequest, Long memberId){

        Post post  = findPostById(postId);
        Member member = findMemberById(memberId);
        Comment comment = commentCreateRequest.toEntity(post, member);

        return commentRepository.save(comment).getId();
    }

    /**
     * 대댓글 작성
     */
    @Transactional
    public Long createChildrenComment(Long postId, Long commentId, ChildrenCreateRequest childrenCreateRequest, Long memberId){

        Post post = findPostById(postId);
        Comment parent = findCommentById(commentId);
        Member member = findMemberById(memberId);

        Comment reply = childrenCreateRequest.toEntity(post, parent, member);

        return commentRepository.save(reply).getId();
    }

    /**
     * 댓글 수정
     */
    @Transactional
    public void modifyComment(Long postId, CommentModifyRequest commentModifyRequest, Long commentId, Long memberId){
        Comment comment = findCommentById(commentId);
        findMemberById(memberId);
        findPostById(postId);

        validateOwner(memberId, comment);

        comment.updateContent(commentModifyRequest.getContent());
    }

    /**
     * 댓글 삭제
     */
    @Transactional
    public void deleteComment(Long postId, Long commentId, Long memberId) {
        Comment comment = findCommentById(commentId);
        findMemberById(memberId);
        findPostById(postId);

        validateOwner(memberId, comment);

        comment.tempDelete();
    }

    /**
     * 자신이 작성한 댓글인지 판단
     */
    private void validateOwner(Long memberId, Comment comment) {
        if (!comment.isOwner(memberId)) {
            throw new CommentAccessDeniedException();
        }
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

    private Member findMemberById(Long memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(RuntimeException::new);
    }

}
