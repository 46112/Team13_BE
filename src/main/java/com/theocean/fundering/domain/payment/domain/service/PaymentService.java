package com.theocean.fundering.domain.payment.domain.service;


import com.siot.IamportRestClient.exception.IamportResponseException;
import com.siot.IamportRestClient.response.IamportResponse;
import com.siot.IamportRestClient.response.Payment;
import com.theocean.fundering.domain.payment.domain.dto.PaymentRequest;
import com.theocean.fundering.domain.payment.domain.repository.PaymentRepository;
import com.theocean.fundering.global.config.PaymentConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class PaymentService {
    private final PaymentConfig paymentConfig;
    private final PaymentRepository paymentRepository;

    public IamportResponse<Payment> verifyByImpUid(String impUid) throws IamportResponseException, IOException {
        return paymentConfig.iamportClient().paymentByImpUid(impUid);
    }

    public void donate(Long postId, PaymentRequest.DonateDTO donateDTO){
    }


}
