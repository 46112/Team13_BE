package com.theocean.fundering.domain.payment.domain.controller;


import com.siot.IamportRestClient.exception.IamportResponseException;
import com.siot.IamportRestClient.response.IamportResponse;
import com.siot.IamportRestClient.response.Payment;
import com.theocean.fundering.domain.payment.domain.dto.PaymentRequest;
import com.theocean.fundering.domain.payment.domain.service.PaymentService;
import com.theocean.fundering.global.utils.ApiUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
    public IamportResponse<Payment> verifyByImpUid(@PathVariable Long postId, @RequestParam(value = "imp_uid") String impUid) throws IamportResponseException, IOException {
        return paymentService.verifyByImpUid(impUid);
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @PostMapping("/posts/{postId}/donate")
    public ResponseEntity<?> donate(@PathVariable Long postId, @RequestBody PaymentRequest.DonateDTO donateDTO){
        paymentService.donate(postId, donateDTO);
        return ResponseEntity.ok(ApiUtils.success(null));
    }


}
