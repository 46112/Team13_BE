package com.theocean.fundering.domain.account.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
public class BalanceResponse {
    private final int balance;

    public BalanceResponse(int balance) {
        this.balance = balance;
    }
}
