package com.theocean.fundering.domain.comment.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

public class CommentRequest {
    @Getter
    @Builder
    public static class SaveDTO {
        @NotBlank(message = "댓글 내용은 필수입니다.")
        private final String content;

        @JsonCreator
        public SaveDTO(@JsonProperty("content") String content) {
            this.content = content;
        }
    }
}
