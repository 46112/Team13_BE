package com.theocean.fundering.domain.account.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Objects;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "account", indexes = @Index(columnList = "post_id"))
@Entity
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long accountId;

    @Column(nullable = false, name = "manager_id")
    private Long managerId;

    @Column(nullable = false, name = "post_id")
    private Long postId;

    @Column(name = "balance")
    private int balance;

    @Builder
    public Account(final Long managerId, final Long postId) {
        this.managerId = managerId;
        this.postId = postId;
        balance = 0;
    }

    public void updateBalance(final int balance) {
        this.balance = balance;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (!(o instanceof final Account account)) return false;
        return Objects.equals(accountId, account.accountId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(accountId);
    }
}