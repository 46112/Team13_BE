package com.theocean.fundering.domain.celebrity.dto;

import com.theocean.fundering.domain.celebrity.domain.Celebrity;
import com.theocean.fundering.domain.celebrity.domain.constant.CelebGender;
import com.theocean.fundering.domain.celebrity.domain.constant.CelebType;
import lombok.Getter;

@Getter
public class CelebDetailsResponseDTO {

    private final String celebName;
    private final CelebGender celebGender;
    private final CelebType celebType;
    private final String celebGroup;
    private final String profileImage;

    private CelebDetailsResponseDTO(final Celebrity celebrity) {
        celebName = celebrity.getCelebName();
        celebGender = celebrity.getCelebGender();
        celebType = celebrity.getCelebType();
        celebGroup = celebrity.getCelebGroup();
        profileImage = celebrity.getProfileImage();
    }
    public static CelebDetailsResponseDTO from(final Celebrity celebrity){
        return new CelebDetailsResponseDTO(celebrity);
    }
}
