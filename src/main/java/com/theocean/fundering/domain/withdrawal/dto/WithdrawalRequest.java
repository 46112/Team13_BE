package com.theocean.fundering.domain.withdrawal.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

public class WithdrawalRequest {

    @Getter
    public static class SaveDTO {
        @NotBlank(message = "사용처 입력은 필수입니다.")
        private final String usage;

        @NotBlank(message = "계좌번호를 입력해주세요.")
        private final String depositAccount;

        @Min(message = "신청 금액은 최소 10000원 이상 입니다.", value = 10000)
        private final int amount;

        public SaveDTO(String usage, String depositAccount, int amount) {
            this.usage = usage;
            this.depositAccount = depositAccount;
            this.amount = amount;
        }
    }
}
