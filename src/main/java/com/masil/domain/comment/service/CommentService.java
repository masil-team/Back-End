package com.masil.domain.comment.service;

import com.masil.domain.comment.dto.*;
import com.masil.domain.comment.entity.Comment;
import com.masil.domain.comment.exception.CommentAccessDeniedException;
import com.masil.domain.comment.exception.CommentNotFoundException;
import com.masil.domain.comment.repository.CommentRepository;
import com.masil.domain.commentlike.repository.CommentLikeRepository;
import com.masil.domain.member.entity.Member;
import com.masil.domain.member.repository.MemberRepository;
import com.masil.domain.post.entity.Post;
import com.masil.domain.post.exception.PostNotFoundException;
import com.masil.domain.post.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final MemberRepository memberRepository;

    private final CommentLikeRepository commentLikeRepository;

    /**
     * 댓글 조회
     */
    public CommentsResponse findComments(Long postId, Pageable pageable, Long memberId){

        Page<Comment> comments = commentRepository.findAllByPostIdAndParentIdNull(postId, pageable);
        log.info("getTotalElements={}", comments.getTotalElements());
        log.info("Size={}", comments.getSize());

        Long totalCommentCount = commentRepository.countByPostId(postId);
        log.info("count = {}", totalCommentCount);

        int totalPage = pageable.getPageSize();
        log.info("totalPage = {}", totalPage);

        for (Comment comment : comments) {
            updateCommentPermissionsForMember(memberId, comment);
            for (Comment child : comment.getChildren()) {
                updateCommentPermissionsForMember(memberId, child);
            }
        }
        return CommentsResponse.ofComment(comments, totalCommentCount);
    }

    /**
     * 댓글 작성
     */
    @Transactional
    public Long createComment(Long postId, CommentCreateRequest commentCreateRequest, Long memberId){

        Post post  = findPostById(postId);
        Member member = findMemberById(memberId);
        Comment comment = commentCreateRequest.toEntity(post, member);
        // 댓글 길이가 250이 넘지 않게
        comment.validateLength(comment.getContent());
        validateOwner(memberId, comment);


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
        comment.validateLength(comment.getContent());

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

//        deleteCommentOrReply(comment);
    }

    /**
     * 자신이 작성한 댓글인지 판단
     */
    private void validateOwner(Long memberId, Comment comment) {
        if (!comment.isOwner(memberId)) {
            throw new CommentAccessDeniedException();
        }
    }

    private void updateCommentPermissionsForMember(Long memberId, Comment comment) {
        boolean isOwner = comment.isCommentWriter(memberId);
        boolean isLiked = commentLikeRepository.existsByCommentAndMemberId(comment, memberId);
        comment.updateIsCommentWriter(isOwner);
        comment.updateCommentLiked(isLiked);
    }


    private void deleteCommentOrReply(Comment comment) {
        if (comment.isParent()) {
            deleteParent(comment);
            return;
        }

        deleteChild(comment);
    }

    private void deleteParent(Comment comment) {
        if (comment.hasNoReply()) {
            comment.tempDelete();
        }
    }

    private void deleteChild(Comment comment) {
        Comment parent = comment.getParent();
        parent.deleteChild(comment);
//        commentRepository.delete(comment);

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
