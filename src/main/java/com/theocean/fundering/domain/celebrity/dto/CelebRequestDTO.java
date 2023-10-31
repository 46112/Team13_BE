package com.theocean.fundering.domain.celebrity.dto;

import com.theocean.fundering.domain.celebrity.domain.Celebrity;
import com.theocean.fundering.domain.celebrity.domain.constant.CelebGender;
import com.theocean.fundering.domain.celebrity.domain.constant.CelebType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@Builder
@RequiredArgsConstructor
@AllArgsConstructor
public class CelebRequestDTO {
    private String celebName;
    private CelebGender celebGender;
    private CelebType celebType;
    private String celebGroup;
    private String profileImage;

    public Celebrity mapToEntity(){
        return Celebrity.builder()
                .celebName(celebName)
                .celebGender(celebGender)
                .celebType(celebType)
                .celebGroup(celebGroup)
                .profileImage(profileImage)
                .build();
    }
}
