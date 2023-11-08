package com.theocean.fundering.domain.myfunding.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Getter
@RequiredArgsConstructor
public class MyFundingSupporterResponseDTO {
    private final Long postId;
    private final String nickname;
    private final String celebName;
    private final String celebImg;
    private final String title;
    private final String thumbnail;
    private final String introduction;
    private final int targetPrice;
    private final int paymentAmount;
    private final LocalDateTime deadline;
    private final LocalDateTime createdAt;
    private final LocalDateTime modifiedAt;
}
