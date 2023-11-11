package com.theocean.fundering.domain.member.dto;

import com.theocean.fundering.domain.celebrity.domain.Celebrity;
import com.theocean.fundering.domain.post.domain.Post;
import com.theocean.fundering.domain.withdrawal.domain.Withdrawal;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

public class MyFundingResponse {

    @Getter
    public static class EmailDTO{
        private final String email;

        private EmailDTO(final String email) {
            this.email = email;
        }

        public static EmailDTO from(String email){
            return new EmailDTO(email);
        }
    }

    @Getter
    public static class FollowingCelebsDTO {
        private final Long celebId;
        private final String profileImage;
        private final String celebName;
        private final int followerCount;

        private FollowingCelebsDTO(final Celebrity celebrity, final int follower) {
            celebId = celebrity.getCelebId();
            profileImage = celebrity.getProfileImage();
            celebName = celebrity.getCelebName();
            followerCount = follower;
        }

        public static FollowingCelebsDTO of(final Celebrity celebrity, final int followerCount) {
            return new FollowingCelebsDTO(celebrity, followerCount);
        }
    }

    @Getter
    @RequiredArgsConstructor
    public static class HostDTO {

        private final Long postId;
        private final String nickname;
        private final String celebName;
        private final String profileImage;
        private final String title;
        private final String thumbnail;
        private final String introduction;
        private final int targetPrice;
        private final LocalDateTime deadline;
        private final LocalDateTime createdAt;
        private final LocalDateTime modifiedAt;
    }

    @Getter
    @RequiredArgsConstructor
    public static class SupporterDTO {
        private final Long postId;
        private final String nickname;
        private final String celebName;
        private final String celebImg;
        private final String title;
        private final String thumbnail;
        private final String introduction;
        private final int targetPrice;
        private final int paymentAmount;
        private final LocalDateTime deadline;
        private final LocalDateTime createdAt;
        private final LocalDateTime modifiedAt;
    }

    @Getter
    @RequiredArgsConstructor
    public static class HeartPostingDTO{
        private final Long postId;
        private final Long writerId;
        private final String nickName;
        private final Long celebId;
        private final String celebName;
        private final String celebImg;
        private final String title;
        private final String thumbnail;
        private final int targetPrice;
        private final int currentAmount;
        private final LocalDateTime deadline;
        private final LocalDateTime createdAt;
        private final LocalDateTime modifiedAt;
        private final int heartCount;
    }

    @Getter
    @RequiredArgsConstructor
    public static class WithdrawalDTO {

        private final Long withdrawalId; // 출금 신청 id
        private final Integer withdrawalAmount;
        private final String usage;
        private final Long postId;
        private final String thumbnail;
        private final String title;
        private final Long userId;
        private final String profileImage;
        private final String nickname;

        private WithdrawalDTO(Withdrawal withdrawal, Post post){
            withdrawalId = withdrawal.getWithdrawalId();
            withdrawalAmount = withdrawal.getWithdrawalAmount();
            usage = withdrawal.getPurpose();
            postId = post.getPostId();
            thumbnail = post.getThumbnail();
            title = post.getTitle();
            userId = post.getWriter().getMemberId();
            profileImage = post.getWriter().getProfileImage();
            nickname = post.getWriter().getNickname();
        }

        public static WithdrawalDTO of(Withdrawal withdrawal, Post post) {
            return new WithdrawalDTO(withdrawal, post);
        }
    }
}
