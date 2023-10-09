package com.theocean.fundering.domain.comment.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.theocean.fundering.domain.comment.domain.Comment;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;


public class CommentResponse {

    @Getter
    public static class findAllDTO {
        private final List<commentsDTO> comments;

        @JsonProperty("isLastPage")
        private final boolean isLastPage;

        public findAllDTO(List<commentsDTO> comments, boolean isLastPage) {
            this.comments = comments;
            this.isLastPage = isLastPage;
        }

        public boolean getIsLastPage() {
            return isLastPage;
        }
    }
    @Getter
    public static class commentsDTO {
        private final Long commentId;
        private final Long writerId;
        private final String writerName;
        private final String writerProfile;
        private final String content;
        private final Long parentCommentOrder;
        private final Long commentOrder;


        @JsonProperty("isDeleted")
        private final boolean isDeleted;
        private final LocalDateTime createdAt;

        public commentsDTO(Comment comment, String writerName, String writerProfile) {
            this.commentId = comment.getCommentId();
            this.writerId = comment.getWriterId();
            this.writerName = writerName;
            this.writerProfile = writerProfile;
            this.content = comment.getContent();
            this.parentCommentOrder = comment.getParentCommentOrder();
            this.commentOrder = comment.getCommentOrder();
            this.isDeleted = comment.isDeleted();
            this.createdAt = comment.getCreatedAt();
        }

        public boolean getIsDeleted() {
            return isDeleted;
        }
    }
}
