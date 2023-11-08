package com.theocean.fundering.domain.post.dto;

import com.theocean.fundering.domain.post.domain.Post;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

public class PostResponse {


    @Getter
    @Setter
    public static class FindByPostIdDTO { // 게시글 열람 DTO
        private Long postId;
        private Long writerId;
        private String writer;
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


        public FindByPostIdDTO(Post post){
            this.postId = post.getPostId();
            this.writerId = post.getWriter().getUserId();
            this.writer = post.getWriter().getNickname();
            this.celebrity = post.getCelebrity().getCelebName();
            this.celebImg = post.getCelebrity().getProfileImage();
            this.title = post.getTitle();
            this.content = post.getIntroduction();
            this.thumbnail = post.getThumbnail();
            this.targetPrice = post.getTargetPrice();
            this.currentAmount = post.getAccount().getBalance();
            this.deadline = post.getDeadline();
            this.createdAt = post.getCreatedAt();
            this.modifiedAt = post.getModifiedAt();
            this.participant = post.getParticipants();
            this.eqWriter = false;
        }
    }


    @Getter
    @Setter
    public static class FindAllDTO{
        private Long postId;
        private Long writerId;
        private String writer;
        private String celebrity;
        private String celebImg;
        private String title;
        private String thumbnail;
        private int targetPrice;
        private int currentAmount;
        private LocalDateTime deadline;
        private LocalDateTime createdAt;
        private LocalDateTime modifiedAt;

        public FindAllDTO(Post post){
            this.postId = post.getPostId();
            this.writerId = post.getWriter().getUserId();
            this.writer = post.getWriter().getNickname();
            this.celebrity = post.getCelebrity().getCelebName();
            this.celebImg = post.getCelebrity().getProfileImage();
            this.title = post.getTitle();
            this.thumbnail = post.getThumbnail();
            this.targetPrice = post.getTargetPrice();
            this.currentAmount = post.getAccount().getBalance();
            this.deadline = post.getDeadline();
            this.createdAt = post.getCreatedAt();
            this.modifiedAt = post.getModifiedAt();
        }
    }

}
