package com.theocean.fundering.domain.post.dto;

import com.theocean.fundering.domain.celebrity.domain.Celebrity;
import com.theocean.fundering.domain.member.domain.Member;
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
        private LocalDateTime deadline;
        private LocalDateTime createdAt;
        private LocalDateTime modifiedAt;
        private int participant;
        private double progress;
        private int difference;


        public FindByPostIdDTO(Post post){
            this.postId = post.getPostId();
            this.writerId = post.getWriter().getUserId();
            this.writer = post.getWriter().getNickname();
            this.celebrity = post.getCelebrity().getCelebName();
            this.celebImg = post.getCelebrity().getProfileImage();
            this.title = post.getTitle();
            this.content = post.getContent();
            this.thumbnail = post.getThumbnail();
            this.targetPrice = post.getTargetPrice();
            this.deadline = post.getDeadline();
            this.createdAt = post.getCreatedAt();
            this.modifiedAt = post.getModifiedAt();
            this.participant = post.getParticipants();
            this.progress = ((double) post.getTargetPrice() / post.getAccount().getFundingAmount()) * 100;
            this.difference = post.getTargetPrice() - post.getAccount().getFundingAmount();
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
        private double progress;
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
            this.progress = ((double) post.getTargetPrice() / post.getAccount().getFundingAmount()) * 100;
            this.deadline = post.getDeadline();
            this.createdAt = post.getCreatedAt();
            this.modifiedAt = post.getModifiedAt();
        }
    }

}
