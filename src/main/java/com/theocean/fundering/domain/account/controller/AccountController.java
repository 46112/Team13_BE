package com.theocean.fundering.domain.account.controller;

import com.theocean.fundering.domain.account.dto.BalanceResponse;
import com.theocean.fundering.domain.account.service.AccountService;
import com.theocean.fundering.domain.comment.dto.CommentResponse;
import com.theocean.fundering.global.utils.ApiUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;

    // (기능) 펀딩 출금가능 금액 조회
    @GetMapping("/posts/{postId}/balance")
    public ResponseEntity<?> getFundingBalance(@PathVariable long postId) {

        final int balance = accountService.getBalance(postId);
        BalanceResponse balanceResponse = new BalanceResponse(balance);

       return ResponseEntity.ok(ApiUtils.success(balanceResponse));
    }
}
