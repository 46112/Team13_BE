package com.theocean.fundering.domain.myfunding.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class MyFundingManagerResponseDTO {

    private final Long withdrawalId; // 출금 신청 id
    private final Long postId;
    private final String thumbnail;
    private final String title;
    private final Long userId;
    private final String profileImage;
    private final String nickname;
    private final Integer withdrawalAmount;
    private final String usage;
}