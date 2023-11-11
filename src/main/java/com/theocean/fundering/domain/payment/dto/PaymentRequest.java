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
    public static class DonateDTO {
        private Integer amount;
        private String impUid;


        @Builder
        public DonateDTO(final Integer amount, final String impUid) {
            this.amount = amount;
            this.impUid = impUid;
        }

        public Payment toEntity(final Member member, final Post post) {
            return Payment.builder()
                    .memberId(member.getMemberId())
                    .postId(post.getPostId())
                    .impUid(impUid)
                    .amount(amount)
                    .build();
        }

    }
}
