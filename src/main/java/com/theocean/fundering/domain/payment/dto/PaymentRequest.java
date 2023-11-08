package com.theocean.fundering.domain.payment.dto;

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
        private String member;
        private Integer amount;


        public Payment toEntity(Member member, Post post, String impUid){
            return Payment.builder()
                    .member(member)
                    .post(post)
                    .impUid(impUid)
                    .amount(amount)
                    .build();
        }
        @Builder
        public DonateDTO(String member, Integer amount){
            this.member = member;
            this.amount = amount;
        }

    }
}
