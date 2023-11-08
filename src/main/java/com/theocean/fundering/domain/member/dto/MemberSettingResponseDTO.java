package com.theocean.fundering.domain.member.dto;

import com.theocean.fundering.domain.member.domain.Member;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class MemberSettingResponseDTO {
    private final String nickname;
    private final int phoneNumber;
    private final String profileImage;

    private MemberSettingResponseDTO(final Member member) {
        nickname = member.getNickname();
        phoneNumber = member.getPhoneNumber();
        profileImage = member.getProfileImage();
    }

    public static MemberSettingResponseDTO from(final Member member) {
        return new MemberSettingResponseDTO(member);
    }
}
