package com.theocean.fundering.domain.payment.controller;


import com.siot.IamportRestClient.exception.IamportResponseException;
import com.siot.IamportRestClient.response.IamportResponse;
import com.siot.IamportRestClient.response.Payment;
import com.theocean.fundering.domain.payment.dto.PaymentRequest;
import com.theocean.fundering.domain.payment.service.PaymentService;
import com.theocean.fundering.global.errors.exception.Exception400;
import com.theocean.fundering.global.jwt.userInfo.CustomUserDetails;
import com.theocean.fundering.global.utils.ApiResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@Tag(name = "PAYMENT", description = "결제 관련 API")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class PaymentController {
    private final PaymentService paymentService;


    @Operation(summary = "결제하기", description = "펀딩 id를 기반으로 펀딩에 결제한다.")
    @PreAuthorize("hasRole('ROLE_USER')")
    @PostMapping("/posts/{postId}/donate")
    @ResponseStatus(HttpStatus.OK)
    public ApiResult<?> donate(@AuthenticationPrincipal final CustomUserDetails userDetails,
                                                @RequestBody final PaymentRequest.DonateDTO donateDTO,
                                                @PathVariable("postId") final Long postId) {
        final String email = userDetails.getEmail();
        paymentService.donate(email, donateDTO, postId);
        return ApiResult.success(null);
    }

}
