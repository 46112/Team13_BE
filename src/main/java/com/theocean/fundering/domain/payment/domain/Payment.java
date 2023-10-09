package com.theocean.fundering.domain.payment.domain;

import com.theocean.fundering.global.utils.AuditingFields;
import com.theocean.fundering.domain.post.domain.Post;
import com.theocean.fundering.domain.member.domain.Member;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;

import java.util.Objects;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;


@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
@Table(name = "Payment")
@Entity
public class Payment extends AuditingFields {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long paymentId;

    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    private Post post;

    // 결제금액
    @Column(nullable = false)
    private Integer amount;

    // 생성자
    @Builder
    public Payment(Member member, Post post, Integer amount) {
        this.member = member;
        this.post = post;
        this.amount = amount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Payment payment)) return false;
        return Objects.equals(paymentId, payment.paymentId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(paymentId);
    }
}
