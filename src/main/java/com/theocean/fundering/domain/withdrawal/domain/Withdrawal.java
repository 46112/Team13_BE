package com.theocean.fundering.domain.withdrawal.domain;

import com.theocean.fundering.global.utils.AuditingFields;
import com.theocean.fundering.domain.post.domain.Post;
import com.theocean.fundering.domain.member.domain.Member;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Table(name = "Withdrawal")
@EntityListeners(AuditingEntityListener.class)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Withdrawal extends AuditingFields {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long withdrawal_id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    private Post post;

    // 사용처
    @Column(nullable = false)
    private String usage;

    // 출금계좌
    @Column(nullable = false)
    private String depositAccount;

    // 출금액
    @Column(nullable = false)
    private Integer withdrawalAmount;

    // 신청일자
    @CreatedDate
    @Column(nullable = false)
    private LocalDateTime createdAt;

    // 승인 여부
    @Column(nullable = false)
    private Boolean isApproved;


    // 생성자
    @Builder
    public Withdrawal(Member member, Post post, String usage, String depositAccount,
                      Integer withdrawalAmount, Boolean isApproved) {
        this.member = member;
        this.post = post;
        this.usage = usage;
        this.depositAccount = depositAccount;
        this.withdrawalAmount = withdrawalAmount;
        this.isApproved = isApproved;
    }

    // Setter Methods
    public void updateUsage(String usage) {
        this.usage = usage;
    }

    public void updateDepositAccount(String depositAccount) {
        this.depositAccount = depositAccount;
    }

    public void updateWithdrawalAmount(Integer withdrawalAmount) {
        this.withdrawalAmount = withdrawalAmount;
    }

    public void updateIsApproved(boolean isApproved) {
        this.isApproved = isApproved;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Withdrawal that)) return false;
        return Objects.equals(withdrawal_id, that.withdrawal_id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(withdrawal_id);
    }
}
