package com.theocean.fundering.domain.celebrity.dto;

import com.theocean.fundering.domain.celebrity.domain.constant.CelebGender;
import com.theocean.fundering.domain.celebrity.domain.constant.CelebType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class CelebListResponseDTO {

    private final Long celebId;
    private final String celebName;
    private final CelebGender celebGender;
    private final CelebType celebType;
    private final String celeGroup;
    private final String profileImage;
}
