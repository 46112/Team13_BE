package com.theocean.fundering.domain.payment.domain.dto;

import com.theocean.fundering.domain.member.domain.Member;
import com.theocean.fundering.domain.payment.domain.Payment;
import com.theocean.fundering.domain.post.domain.Post;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

public class PaymentRequest {

    @Getter
    @Setter
    @NoArgsConstructor
    @ToString
    public static class DonateDTO{
        private Long userId;
        private String member;
        private Long postId;
        private Integer amount;


        public Payment toEntity(Member member, Post post){
            return Payment.builder()
                    .member(member)
                    .post(post)
                    .amount(amount)
                    .build();
        }
        @Builder
        public DonateDTO(Long userId, String member, Long postId, Integer amount){
            this.userId = userId;
            this.member = member;
            this.postId = postId;
            this.amount = amount;
        }

    }
}
