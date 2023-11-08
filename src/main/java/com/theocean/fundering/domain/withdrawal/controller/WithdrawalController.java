package com.theocean.fundering.domain.withdrawal.controller;

import com.theocean.fundering.domain.withdrawal.dto.WithdrawalRequest;
import com.theocean.fundering.domain.withdrawal.dto.WithdrawalResponse;
import com.theocean.fundering.domain.withdrawal.service.WithdrawalService;
import com.theocean.fundering.global.jwt.userInfo.CustomUserDetails;
import com.theocean.fundering.global.utils.ApiUtils;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class WithdrawalController {

    private final WithdrawalService withdrawalService;

    // (기능) 출금 신청하기
    @PostMapping("/posts/{postId}/withdrawals")
    public ResponseEntity<?> applyWithdrawal(
            @AuthenticationPrincipal final CustomUserDetails userDetails,
            @RequestBody @Valid final WithdrawalRequest.SaveDTO request,
            @PathVariable final long postId) {

        final Long memberId = 1L; // Long memberId = userDetails.getId();
        withdrawalService.applyWithdrawal(memberId, postId, request);

        return ResponseEntity.ok(ApiUtils.success(null));
    }

    // (기능) 출금내역 조회
    @GetMapping("/posts/{postId}/withdrawals")
    public ResponseEntity<?> readWithdrawals(@PathVariable final long postId, @PageableDefault(size = 10) Pageable pageable) {

        final WithdrawalResponse.FindAllDTO response = withdrawalService.getWithdrawals(postId, pageable);

        return ResponseEntity.ok(ApiUtils.success(response));
    }
}
