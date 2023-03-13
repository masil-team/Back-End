package com.masil.domain.member.controller;

import com.masil.domain.bookmark.service.BookmarkService;
import com.masil.domain.comment.dto.CommentsResponse;
import com.masil.domain.comment.service.CommentService;
import com.masil.domain.commentlike.service.CommentLikeService;
import com.masil.domain.member.dto.request.MyFindRequest;
import com.masil.domain.post.dto.PostsResponse;
import com.masil.domain.post.service.PostService;
import com.masil.domain.postlike.service.PostLikeService;
import com.masil.global.auth.annotaion.LoginUser;
import com.masil.global.auth.dto.response.CurrentMember;
import com.masil.global.error.exception.BusinessException;
import com.masil.global.error.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.data.domain.Sort.Direction.DESC;

@RestController
@RequestMapping("/my")
@RequiredArgsConstructor
public class MyController {

    private final PostService postService;
    private final PostLikeService postLikeService;
    private final CommentLikeService commentLikeService;
    private final CommentService commentService;
    private final BookmarkService bookmarkService;

    // TODO: 2023/03/06 본인 글 조회
    /*
        1. 본인글 조회
        2. 본인 좋아요 누른 글 조회
        3. 즐겨찾기 한 게시글
        4. 본인 댓글
     */

    @GetMapping("/posts")
    public ResponseEntity<PostsResponse> findMyPosts(@LoginUser CurrentMember currentMember,
                                                     @RequestBody MyFindRequest request,
                                                     @PageableDefault(sort = "createDate", direction = DESC) Pageable pageable) {
        validateMember(currentMember, request);
        return ResponseEntity.ok(postService.findPostsByMember(request, pageable));
    }

    private static void validateMember(CurrentMember currentMember, MyFindRequest request) {
        if (currentMember == null) {
            throw new BusinessException(ErrorCode.ACCEESS_DENIED_MEMBER);
        } else if (!currentMember.getId().equals(request.getMemberId())) {
            throw new BusinessException(ErrorCode.ACCEESS_DENIED_MEMBER);
        }
    }

    @GetMapping("/post-likes")
    public ResponseEntity<PostsResponse> findMyLikesAboutPost(@LoginUser CurrentMember currentMember,
                                                        @RequestBody MyFindRequest request,
                                                        @PageableDefault(sort = "createDate", direction = DESC) Pageable pageable) {
        validateMember(currentMember, request);
        return ResponseEntity.ok(postLikeService.findLikesByMemberId(request, pageable));
    }

    @GetMapping("/comment-likes")
    public ResponseEntity<CommentsResponse> findMyLikesAboutComment(@LoginUser CurrentMember currentMember,
                                                                    @RequestBody MyFindRequest request,
                                                                    @PageableDefault(sort = "createDate", direction = DESC) Pageable pageable) {
        validateMember(currentMember, request);
        return ResponseEntity.ok(commentLikeService.findLikesByMemberId(request,pageable));
    }

    @GetMapping("/bookmarks")
    public ResponseEntity<PostsResponse> findMyBookmarks(@LoginUser CurrentMember currentMember,
                                                         @RequestBody MyFindRequest request,
                                                         @PageableDefault(sort = "createDate", direction = DESC) Pageable pageable) {
        validateMember(currentMember, request);
        return ResponseEntity.ok(bookmarkService.findBookmarksByMember(request, pageable));
    }

    @GetMapping("/comments")
    public ResponseEntity<CommentsResponse> findMyComments(@LoginUser CurrentMember currentMember,
                                                           @RequestBody MyFindRequest request,
                                                           @PageableDefault(sort = "createDate", direction = DESC) Pageable pageable) {
        validateMember(currentMember, request);
        return ResponseEntity.ok(commentService.findCommentsByMemberId(request.getMemberId(), pageable));
    }

}
