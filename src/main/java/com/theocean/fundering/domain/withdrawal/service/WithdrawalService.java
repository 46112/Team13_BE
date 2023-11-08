package com.theocean.fundering.domain.withdrawal.service;

import com.theocean.fundering.domain.account.domain.Account;
import com.theocean.fundering.domain.account.repository.AccountRepository;
import com.theocean.fundering.domain.comment.domain.Comment;
import com.theocean.fundering.domain.comment.dto.CommentResponse;
import com.theocean.fundering.domain.evidence.domain.Evidence;
import com.theocean.fundering.domain.evidence.repository.EvidenceRepository;
import com.theocean.fundering.domain.member.domain.Member;
import com.theocean.fundering.domain.withdrawal.domain.Withdrawal;
import com.theocean.fundering.domain.withdrawal.dto.WithdrawalRequest;
import com.theocean.fundering.domain.withdrawal.dto.WithdrawalResponse;
import com.theocean.fundering.domain.withdrawal.repository.WithdrawalRepository;
import com.theocean.fundering.global.errors.exception.Exception404;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class WithdrawalService {

    private final WithdrawalRepository withdrawalRepository;
    private final AccountRepository accountRepository;
    private final EvidenceRepository evidenceRepository;

    // 출금 신청 저장 로직
    @Transactional
    public void applyWithdrawal(final Long memberId, final Long postId, final WithdrawalRequest.SaveDTO request) {
        final Withdrawal withdrawal = Withdrawal.builder()
                .applicantId(memberId)
                .postId(postId)
                .usage(request.getUsage())
                .depositAccount(request.getDepositAccount())
                .withdrawalAmount(request.getAmount())
                .build();

        withdrawalRepository.save(withdrawal);
    }

    public WithdrawalResponse.FindAllDTO getWithdrawals(final Long postId, Pageable pageable){
        Account account = accountRepository.findByPostId(postId)
                .orElseThrow(() -> new Exception404("계좌가 존재하지 않습니다."));
        int currentBalance = account.getBalance();

        Page<Withdrawal> withdrawalPage = withdrawalRepository.getWithdrawalPage(postId, pageable);
        List<Withdrawal> withdrawals = withdrawalPage.getContent();

        final List<WithdrawalResponse.WithdrawalDTO> withdrawalDTOs = convertToWithdrawalDTOs(withdrawals);

        int currentPage = pageable.getPageNumber();
        boolean isLastPage = withdrawalPage.isLast();

        return new WithdrawalResponse.FindAllDTO(account.getBalance(), withdrawalDTOs, currentPage, isLastPage);
    }


    private List<WithdrawalResponse.WithdrawalDTO> convertToWithdrawalDTOs(final List<Withdrawal> withdrawals) {
        return withdrawals.stream().map(this::createWithdrawalDTO).collect(Collectors.toList());
    }

    private WithdrawalResponse.WithdrawalDTO createWithdrawalDTO(final Withdrawal withdrawal) {
        Optional<Evidence> evidence = evidenceRepository.findByWithdrawalId(withdrawal.getWithdrawalId());
        String evidenceURL = null;
        if (evidence.isPresent()) evidenceURL = evidence.get().getUrl();

        return WithdrawalResponse.WithdrawalDTO.fromEntity(withdrawal, evidenceURL);
    }
}
