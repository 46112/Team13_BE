package com.theocean.fundering.domain.comment.controller;

import com.theocean.fundering.domain.comment.dto.CommentRequest;
import com.theocean.fundering.domain.comment.dto.CommentResponse;
import com.theocean.fundering.domain.comment.service.CreateCommentService;
import com.theocean.fundering.domain.comment.service.DeleteCommentService;
import com.theocean.fundering.domain.comment.service.ReadCommentService;
import com.theocean.fundering.global.jwt.userInfo.CustomUserDetails;
import com.theocean.fundering.global.utils.ApiUtils;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class CommentController {

    private final CreateCommentService createCommentService;
    private final ReadCommentService readCommentService;
    private final DeleteCommentService deleteCommentService;

    // (기능) 댓글 작성
    @PreAuthorize("hasRole('USER')")
    @PostMapping("/posts/{postId}/comments")
    public ResponseEntity<?> createComment(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestBody @Valid CommentRequest.SaveDTO commentRequest,
            @PathVariable long postId) {

        Long memberId = 1L; // Long memberId = userDetails.getMember().getUserId();
        createCommentService.createComment(memberId, postId, commentRequest);

        return ResponseEntity.ok(ApiUtils.success(null));
    }

    // (기능) 대댓글 작성
    @PreAuthorize("hasRole('USER')")
    @PostMapping("/posts/{postId}/comments/{commentId}")
    public ResponseEntity<?> createSubComment(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestBody @Valid CommentRequest.SaveDTO commentRequest,
            @PathVariable long postId,
            @PathVariable long commentId) {

        Long memberId = 1L; // Long memberId = userDetails.getMember().getUserId();
        createCommentService.createSubComment(memberId, postId, commentId, commentRequest);

        return ResponseEntity.ok(ApiUtils.success(null));
    }

    // (기능) 댓글 목록 조회
    @GetMapping("/posts/{postId}/comments")
    public ResponseEntity<?> readComments(
            @PathVariable long postId, @PageableDefault(size = 10) Pageable pageable) {

        CommentResponse.FindAllDTO response = readCommentService.getComments(postId, pageable);

        return ResponseEntity.ok(ApiUtils.success(response));
    }

    // (기능) 대댓글 목록 조회
    @GetMapping("/posts/{postId}/comments/{commentId}")
    public ResponseEntity<?> readSubComments(
            @PathVariable long postId,
            @PathVariable long commentId,
            @PageableDefault(size = 10) Pageable pageable) {

        CommentResponse.FindAllDTO response =
                readCommentService.getSubComments(postId, commentId, pageable);

        return ResponseEntity.ok(ApiUtils.success(response));
    }

    // (기능) 댓글 삭제
    @PreAuthorize("hasRole('USER')")
    @DeleteMapping("/posts/{postId}/comments/{commentId}")
    public ResponseEntity<?> deleteComment(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable long postId,
            @PathVariable long commentId) {

        Long memberId = 1L; // userDetails.getMember().getUserId();
        deleteCommentService.deleteComment(memberId, postId, commentId);

        return ResponseEntity.ok(ApiUtils.success(null));
    }
}
