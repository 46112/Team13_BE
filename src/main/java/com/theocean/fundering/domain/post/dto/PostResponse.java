package com.theocean.fundering.domain.post.dto;

import com.theocean.fundering.domain.post.domain.Post;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

public class PostResponse {


    @Getter
    @Setter
    public static class FindByPostIdDTO { // 게시글 열람 DTO
        private Long postId;
        private Long writerId;
        private String writer;
        private String writerImg;
        private Long celebId;
        private String celebrity;
        private String celebImg;
        private String title;
        private String content;
        private String thumbnail;
        private int targetPrice;
        private int currentAmount;
        private LocalDateTime deadline;
        private LocalDateTime createdAt;
        private LocalDateTime modifiedAt;
        private int participant;
        private boolean eqWriter;
        private int heartCount;
        private boolean isFollowed;
        private boolean isHeart;

        public FindByPostIdDTO(final Post post) {
            postId = post.getPostId();
            writerId = post.getWriter().getMemberId();
            writer = post.getWriter().getNickname();
            writerImg = post.getWriter().getProfileImage();
            celebId = post.getCelebrity().getCelebId();
            celebrity = post.getCelebrity().getCelebName();
            celebImg = post.getCelebrity().getProfileImage();
            title = post.getTitle();
            content = post.getIntroduction();
            thumbnail = post.getThumbnail();
            targetPrice = post.getTargetPrice();
            currentAmount = post.getAccount().getBalance();
            deadline = post.getDeadline();
            createdAt = post.getCreatedAt();
            modifiedAt = post.getModifiedAt();
            participant = post.getParticipants();
            eqWriter = false;
            heartCount = post.getHeartCount();
            isFollowed = false;
            isHeart = false;
        }
    }

    @Getter
    @Setter
    @NoArgsConstructor
    public static class FindAllDTO {
        private Long postId;
        private Long writerId;
        private String writer;
        private Long celebId;
        private String celebrity;
        private String celebImg;
        private String title;
        private String thumbnail;
        private int targetPrice;
        private int currentAmount;
        private LocalDateTime deadline;
        private LocalDateTime createdAt;
        private LocalDateTime modifiedAt;
        private int heartCount;
        private boolean isHeart;

        public FindAllDTO(final Post post) {
            postId = post.getPostId();
            writerId = post.getWriter().getMemberId();
            writer = post.getWriter().getNickname();
            celebId = post.getCelebrity().getCelebId();
            celebrity = post.getCelebrity().getCelebName();
            celebImg = post.getCelebrity().getProfileImage();
            title = post.getTitle();
            thumbnail = post.getThumbnail();
            targetPrice = post.getTargetPrice();
            currentAmount = post.getAccount().getBalance();
            deadline = post.getDeadline();
            createdAt = post.getCreatedAt();
            modifiedAt = post.getModifiedAt();
            heartCount = post.getHeartCount();
            isHeart = false;
        }
    }

}
