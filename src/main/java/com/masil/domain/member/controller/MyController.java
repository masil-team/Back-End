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
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.data.domain.Sort.Direction.DESC;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class MyController {

    private final PostService postService;
    private final PostLikeService postLikeService;
    private final CommentLikeService commentLikeService;
    private final CommentService commentService;
    private final BookmarkService bookmarkService;

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/members/{memberId}/posts")
    public ResponseEntity<PostsResponse> findMyPosts(@LoginUser CurrentMember currentMember,
                                                     @PathVariable Long memberId,
                                                     @PageableDefault(sort = "createDate", direction = DESC) Pageable pageable) {

        MyFindRequest request = MyFindRequest.builder()
                .memberId(memberId).build();
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

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/members/{memberId}/like-posts")
    public ResponseEntity<PostsResponse> findMyLikesAboutPost(@LoginUser CurrentMember currentMember,
                                                              @PathVariable Long memberId,
                                                        @PageableDefault(sort = "createDate", direction = DESC) Pageable pageable) {
        MyFindRequest request = MyFindRequest.builder()
                .memberId(memberId).build();
        validateMember(currentMember, request);
        return ResponseEntity.ok(postLikeService.findLikesByMemberId(request, pageable));
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/members/{memberId}/like-comments")
    public ResponseEntity<CommentsResponse> findMyLikesAboutComment(@LoginUser CurrentMember currentMember,
                                                                    @PathVariable Long memberId,
                                                                    @PageableDefault(sort = "createDate", direction = DESC) Pageable pageable) {
        MyFindRequest request = MyFindRequest.builder()
                .memberId(memberId).build();
        validateMember(currentMember, request);
        return ResponseEntity.ok(commentLikeService.findLikesByMemberId(request,pageable));
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/members/{memberId}/bookmarks")
    public ResponseEntity<PostsResponse> findMyBookmarks(@LoginUser CurrentMember currentMember,
                                                         @PathVariable Long memberId,
                                                         @PageableDefault(sort = "createDate", direction = DESC) Pageable pageable) {
        MyFindRequest request = MyFindRequest.builder()
                .memberId(memberId).build();
        validateMember(currentMember, request);
        return ResponseEntity.ok(bookmarkService.findBookmarksByMember(request, pageable));
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/members/{memberId}/comments")
    public ResponseEntity<CommentsResponse> findMyComments(@LoginUser CurrentMember currentMember,
                                                           @PathVariable Long memberId,
                                                           @PageableDefault(sort = "createDate", direction = DESC) Pageable pageable) {
        MyFindRequest request = MyFindRequest.builder()
                .memberId(memberId).build();
        validateMember(currentMember, request);
        return ResponseEntity.ok(commentService.findCommentsByMemberId(request.getMemberId(), pageable));
    }

}
