package com.theocean.fundering.domain.comment.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;


public class CommentRequest {
    @Getter
    @Builder
    @RequiredArgsConstructor
    public static class saveDTO {
        @NotBlank(message = "댓글 내용은 필수입니다.")
        private final String content;
        private final Long parentCommentOrder;  // 대댓글의 경우 부모 댓글의 commentOrder. 일반 댓글은 null.
    }

}
