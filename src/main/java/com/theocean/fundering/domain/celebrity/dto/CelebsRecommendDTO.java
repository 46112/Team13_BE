package com.theocean.fundering.domain.celebrity.dto;

import com.theocean.fundering.domain.celebrity.domain.Celebrity;
import lombok.Getter;

@Getter
public class CelebsRecommendDTO {

    private final Long celebId;
    private final String celebName;
    private final String celebProfile;
    private final int followingCount;
    private final boolean isFollowing;

    private CelebsRecommendDTO(final Celebrity celebrity, int followerCount, boolean following) {
        celebId = celebrity.getCelebId();
        celebName = celebrity.getCelebName();
        celebProfile = celebrity.getProfileImage();
        followingCount = followerCount;
        isFollowing = following;
    }
    public static CelebsRecommendDTO of(Celebrity celebrity, int followerCount, boolean following) {
        return new CelebsRecommendDTO(celebrity, followerCount, following);
    }
}
