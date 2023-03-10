package com.masil.domain.comment.service;

import com.masil.domain.comment.dto.*;
import com.masil.domain.comment.entity.Comment;
import com.masil.domain.comment.exception.CommentAccessDeniedException;
import com.masil.domain.comment.exception.CommentNotFoundException;
import com.masil.domain.comment.repository.CommentRepository;
import com.masil.domain.commentlike.repository.CommentLikeRepository;
import com.masil.domain.member.entity.Member;
import com.masil.domain.member.repository.MemberRepository;
import com.masil.domain.notification.dto.NotificationDto;
import com.masil.domain.notification.entity.NotificationType;
import com.masil.domain.notification.service.NotificationService;
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
    private final NotificationService notificationService;

    /**
     * ๋๊ธ ์กฐํ
     */
    public CommentsResponse findComments(Long postId, Pageable pageable, Long memberId){

        Page<Comment> comments = commentRepository.findAllByPostId(postId, pageable);
        
        Long totalCommentCount = commentRepository.countByPostId(postId);

        int totalPage = comments.getTotalPages();

        for (Comment comment : comments) {
            log.info("comment id = {}", comment.getId());
            updateCommentPermissionsForMember(memberId, comment);
            for (Comment child : comment.getChildren()) {
                updateCommentPermissionsForMember(memberId, child);
            }
        }

        return CommentsResponse.ofComment(comments, totalCommentCount, totalPage);
    }

    /**
     * ๋๊ธ ์์ฑ
     */
    @Transactional
    public Long createComment(Long postId, CommentCreateRequest commentCreateRequest, Long memberId){

        Post post  = findPostById(postId);
        Member member = findMemberById(memberId);
        Comment comment = commentCreateRequest.toEntity(post, member);
        // ๋๊ธ ๊ธธ์ด๊ฐ 250์ด ๋์ง ์๊ฒ
        comment.validateLength(comment.getContent());
        validateOwner(memberId, comment);

        notificationService.send(member, post.getMember(), NotificationDto.of(NotificationType.POST_COMMENT_REPLY, post));

        return commentRepository.save(comment).getId();
    }

    /**
     * ๋๋๊ธ ์์ฑ
     */
    @Transactional
    public Long createChildrenComment(Long postId, Long commentId, ChildrenCreateRequest childrenCreateRequest, Long memberId){

        Post post = findPostById(postId);
        Comment parent = findCommentById(commentId);
        Member member = findMemberById(memberId);

        Comment reply = childrenCreateRequest.toEntity(post, parent, member);

        notificationService.send(member, parent.getMember(), NotificationDto.of(NotificationType.COMMENT_REPLY, post));

        return commentRepository.save(reply).getId();
    }

    /**
     * ๋๊ธ ์์?
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
     * ๋๊ธ ์ญ์?
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
     * ์์?์ด ์์ฑํ ๋๊ธ์ธ์ง ํ๋จ
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

    /**
     * ์์ธ ์ฒ๋ฆฌ
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
