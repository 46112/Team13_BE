package com.theocean.fundering.domain.celebrity.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class CelebFundingResponseDTO {

    private final Long postId;
    private final Long userId;
    private final String nickname;
    private final Long celebId;
    private final String celebName;
    private final String title;
    private final String content;
    private final int participants;
    private final int targetPrice;
}
