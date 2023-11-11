package com.theocean.fundering.domain.account.controller;

import com.theocean.fundering.domain.account.dto.BalanceResponse;
import com.theocean.fundering.domain.account.service.AccountService;
import com.theocean.fundering.global.utils.ApiResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "ACCOUNT", description = "펀딩 계좌 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class AccountController {

    private final AccountService accountService;

    // (기능) 펀딩 출금가능 금액 조회
    @Operation(summary = "펀딩 출금가능 금액 조회", description = "postId에 해당하는 게시글의 Account의 출금가능 금액을 조회한다.", responses = @ApiResponse(responseCode = "200", content = @Content(schema = @Schema(implementation = BalanceResponse.class))))
    @GetMapping("/posts/{postId}/balance")
    @ResponseStatus(HttpStatus.OK)
    public ApiResult<BalanceResponse> getFundingBalance(
            @Parameter(description = "게시글의 PK") @PathVariable final long postId
    ) {
        final int balance = accountService.getBalance(postId);
        final var balanceResponse = new BalanceResponse(balance);

        return ApiResult.success(balanceResponse);
    }
}