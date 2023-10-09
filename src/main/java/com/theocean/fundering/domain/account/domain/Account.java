package com.theocean.fundering.domain.account.domain;

import com.theocean.fundering.domain.post.domain.Post;
import com.theocean.fundering.domain.member.domain.Member;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Objects;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "account")
@Entity
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long accountId;

    @ManyToOne
    private Member member;

    @OneToOne
    private Post post;

    @Column
    private int fundingAmount;

    @Builder
    public Account(Member member, Post post, int fundingAmount) {
        this.member = member;
        this.post = post;
        this.fundingAmount = fundingAmount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Account account)) return false;
        return Objects.equals(accountId, account.accountId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(accountId);
    }
}