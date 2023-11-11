package com.theocean.fundering.domain.payment.service;


import com.siot.IamportRestClient.exception.IamportResponseException;
import com.siot.IamportRestClient.request.CancelData;
import com.siot.IamportRestClient.response.IamportResponse;
import com.siot.IamportRestClient.response.Payment;
import com.theocean.fundering.domain.account.domain.Account;
import com.theocean.fundering.domain.member.domain.Member;
import com.theocean.fundering.domain.member.repository.MemberRepository;
import com.theocean.fundering.domain.payment.dto.PaymentRequest;
import com.theocean.fundering.domain.payment.repository.PaymentRepository;
import com.theocean.fundering.domain.post.domain.Post;
import com.theocean.fundering.domain.post.repository.PostRepository;
import com.theocean.fundering.global.config.PaymentConfig;
import com.theocean.fundering.global.errors.exception.ErrorCode;
import com.theocean.fundering.global.errors.exception.Exception500;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class PaymentService {
    private final PaymentConfig paymentConfig;
    private final PaymentRepository paymentRepository;
    private final PostRepository postRepository;
    private final MemberRepository memberRepository;


    @Transactional
    public IamportResponse<Payment> verifyByImpUidAndDonate(final String email,
                                                            final PaymentRequest.DonateDTO dto,
                                                            final Long postId) throws IamportResponseException, IOException {
        IamportResponse<Payment> iamportResponse = paymentConfig.iamportClient().paymentByImpUid(dto.getImpUid());
        if (iamportResponse.getResponse().getAmount().intValue() == dto.getAmount()){
            final Member member = memberRepository.findByEmail(email).orElseThrow(
                    () -> new Exception500(ErrorCode.ER01)
            );
            final Post post = postRepository.findById(postId).orElseThrow(
                    () -> new Exception500(ErrorCode.ER03)
            );
            final Account account = post.getAccount();
            account.updateBalance(account.getBalance() + dto.getAmount());
            paymentRepository.save(dto.toEntity(member, post));
        }
        else {
            CancelData cancelData = new CancelData(dto.getImpUid(), true);
            iamportResponse = paymentConfig.iamportClient().cancelPaymentByImpUid(cancelData);
        }
        return iamportResponse;
    }

}
