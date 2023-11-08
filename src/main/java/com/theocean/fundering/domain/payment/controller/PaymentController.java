package com.theocean.fundering.domain.payment.controller;


import com.siot.IamportRestClient.exception.IamportResponseException;
import com.siot.IamportRestClient.response.IamportResponse;
import com.siot.IamportRestClient.response.Payment;
import com.theocean.fundering.domain.member.domain.Member;
import com.theocean.fundering.domain.payment.dto.PaymentRequest;
import com.theocean.fundering.domain.payment.service.PaymentService;
import com.theocean.fundering.global.jwt.JwtProvider;
import com.theocean.fundering.global.jwt.userInfo.CustomUserDetails;
import com.theocean.fundering.global.utils.ApiUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RequiredArgsConstructor
@RestController
public class PaymentController {
    private final PaymentService paymentService;

    @PreAuthorize("hasRole('ROLE_USER')")
    @PostMapping("/posts/{postId}/verify")
    public IamportResponse<Payment> verifyByImpUid(@PathVariable Long postId,
                                                   @RequestParam(value = "imp_uid") String impUid) throws IamportResponseException, IOException {
        return paymentService.verifyByImpUid(impUid);
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @PostMapping("/posts/{postId}/donate")
    public ResponseEntity<?> donate(@PathVariable Long postId,
                                    @AuthenticationPrincipal CustomUserDetails userDetails,
                                    @RequestParam(value = "imp_uid") String impUid,
                                    @RequestBody PaymentRequest.DonateDTO donateDTO){
        String email = userDetails.getEmail();
        paymentService.donate(postId, email, impUid, donateDTO);
        return ResponseEntity.ok(ApiUtils.success(null));
    }


}
