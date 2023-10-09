package com.theocean.fundering.domain.comment.controller;

import com.theocean.fundering.domain.comment.dto.CommentRequest;
import com.theocean.fundering.domain.comment.dto.CommentResponse;
import com.theocean.fundering.domain.comment.service.CommentService;
import com.theocean.fundering.domain.comment.domain.Comment;
import com.theocean.fundering.global.utils.ApiUtils;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    // (기능) 댓글 작성
    @PostMapping("/posts/{postId}/comments")
    public ResponseEntity<?> createComment(@RequestBody @Valid CommentRequest.saveDTO commentRequest, @PathVariable long postId) {
        // TODO: memberId 부분은 authentication를 통해 작성자 확인 필요
        commentService.createComment(1L, postId, commentRequest);
        return ResponseEntity.ok(ApiUtils.success(null));
    }

    // (기능) 댓글 목록 조회
    @GetMapping("/posts/{postId}/comments")
    public ResponseEntity<?> getComments(@PathVariable long postId,
                                         @RequestParam(required = false, defaultValue = "0") int pageIndex,
                                         @RequestParam(required = false, defaultValue = "3") int pageSize) {

        CommentResponse.findAllDTO response = commentService.getCommentsDtoByPostId(postId, PageRequest.of(pageIndex, pageSize));
        return ResponseEntity.ok(ApiUtils.success(response));
    }

    // (기능) 댓글 삭제
    @DeleteMapping("/posts/{postId}/comments/{commentId}")
    public ResponseEntity<?> deleteComment(@PathVariable long postId, @PathVariable long commentId) {
        // TODO: authentication를 통해 작성자 확인 필요
        Long memberId = 1L;  // 임시 코드, 실제로는 인증 정보에서 가져와야 함
        commentService.deleteComment(memberId, postId, commentId);
        return ResponseEntity.ok(ApiUtils.success(null));
    }
}