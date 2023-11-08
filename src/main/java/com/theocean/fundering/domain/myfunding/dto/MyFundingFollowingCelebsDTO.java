package com.theocean.fundering.domain.myfunding.dto;

import com.theocean.fundering.domain.celebrity.domain.Celebrity;
import lombok.Getter;

@Getter
public class MyFundingFollowingCelebsDTO {
    private final Long celebId;
    private final String profileImage;
    private final String celebName;
    private final int followerCount;

    private MyFundingFollowingCelebsDTO(final Celebrity celebrity, final int follower) {
        celebId = celebrity.getCelebId();
        profileImage = celebrity.getProfileImage();
        celebName = celebrity.getCelebName();
        this.followerCount = follower;
    }

    public static MyFundingFollowingCelebsDTO of(final Celebrity celebrity, final int followerCount) {
        return new MyFundingFollowingCelebsDTO(celebrity, followerCount);
    }
}
