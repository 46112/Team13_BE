package com.theocean.fundering.global.oauth2;

import com.theocean.fundering.domain.member.domain.constant.UserRole;
import com.theocean.fundering.global.oauth2.userInfo.KakaoOAuth2UserInfo;
import com.theocean.fundering.global.oauth2.userInfo.OAuth2UserInfo;
import com.theocean.fundering.domain.member.domain.Member;
import lombok.Builder;
import lombok.Getter;

import java.util.Map;

@Getter
public class OAuthAttributes {

    private String nameAttributeKey;
    private OAuth2UserInfo oauth2UserInfo;

    @Builder
    public OAuthAttributes(String nameAttributeKey, OAuth2UserInfo oauth2UserInfo) {
        this.nameAttributeKey = nameAttributeKey;
        this.oauth2UserInfo = oauth2UserInfo;
    }

    public static OAuthAttributes of(String userNameAttributeName, Map<String, Object> attributes) {
        return ofKakao(userNameAttributeName, attributes);
    }

    private static OAuthAttributes ofKakao(String userNameAttributeName, Map<String, Object> attributes) {
        return OAuthAttributes.builder()
                .nameAttributeKey(userNameAttributeName)
                .oauth2UserInfo(new KakaoOAuth2UserInfo(attributes))
                .build();
    }

    public Member toEntity(OAuth2UserInfo oauth2UserInfo) {
        return Member.builder()
                .userId(Long.valueOf(oauth2UserInfo.getId()))
                .nickname(oauth2UserInfo.getNickname())
                .profileImage(oauth2UserInfo.getImageUrl())
                // .phoneNumber(oauth2UserInfo.getPhoneNumber())
                .userRole(UserRole.USER)
                .build();
    }
}