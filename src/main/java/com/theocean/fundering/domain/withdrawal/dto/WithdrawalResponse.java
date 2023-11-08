package com.theocean.fundering.domain.withdrawal.dto;

import com.theocean.fundering.domain.comment.domain.Comment;
import com.theocean.fundering.domain.withdrawal.domain.Withdrawal;
import lombok.Getter;

import java.util.List;

public class WithdrawalResponse {

    @Getter
    public static class FindAllDTO {
        private final int currentBalance;
        private final List<WithdrawalDTO> withdrawalDTOs;
        private final int currentPage;
        private final boolean isLastPage;

        public FindAllDTO(final int currentBalance, final List<WithdrawalDTO> withdrawalDTOs, final int currentPage, final boolean isLastPage) {
            this.currentBalance = currentBalance;
            this.withdrawalDTOs = withdrawalDTOs;
            this.currentPage = currentPage;
            this.isLastPage = isLastPage;
        }

        public boolean getIsLastPage() {
            return isLastPage;
        }
    }

    @Getter
    public static class WithdrawalDTO {
        private final Long withdrawalId;
        private final String usage;
        private final int depositAmount;
        private final Integer balance;
        private final String evidence;
        private final Long depositTime;


        WithdrawalDTO(final Withdrawal withdrawal, final String evidenceURL) {
            this.withdrawalId = withdrawal.getWithdrawalId();
            this.usage = withdrawal.getUsage();
            this.depositAmount = withdrawal.getWithdrawalAmount();
            this.balance = withdrawal.getBalance();
            this.evidence = evidenceURL;
            this.depositTime = withdrawal.getDepositTime();
        }

        public static WithdrawalDTO fromEntity(final Withdrawal withdrawal, final String evidenceURL) {
            return new WithdrawalDTO(withdrawal, evidenceURL);
        }
    }
}
