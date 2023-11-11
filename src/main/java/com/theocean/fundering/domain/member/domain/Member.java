package com.theocean.fundering.domain.member.domain;

import com.theocean.fundering.domain.member.domain.constant.MemberRole;
import com.theocean.fundering.global.utils.AuditingFields;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.Objects;


@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "member",
        indexes = @Index(columnList = "email", unique = true)
)
@Entity
public class Member extends AuditingFields {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long memberId;

    @Setter
    @Column(nullable = false, length = 15, name = "nickname")
    private String nickname;

    @Column(nullable = false, name = "password")
    private String password;

    @Column(nullable = false, length = 50, name = "email")
    private String email;

    @Column(length = 11, name = "phone_number")
    private String phoneNumber;

    @Enumerated(EnumType.STRING)
    @Column(name = "user_role")
    private MemberRole memberRole;

    @Column(name = "refresh_token")
    private String refreshToken; // 리프레시 토큰

    @Column(name = "profile_image")
    private String profileImage; // 프로필 이미지

    private void changeNickname(final String nickname){
        this.nickname = nickname;
    }

    public void setPassword(final String password){
        this.password = password;
    }

    public Member updateRefreshToken(final String updateRefreshToken) {
        refreshToken = updateRefreshToken;
        return this;
    }

    private void changePhoneNumber(final String phoneNumber){
        this.phoneNumber = phoneNumber;
    }


    private void changeProfileImage(final String profileImage) {
        this.profileImage = profileImage;
    }

    @Builder
    public Member(final Long memberId, final String nickname, final String password, final String email, final MemberRole memberRole, final String profileImage) {
        this.memberId = memberId;
        this.nickname = nickname;
        this.password = password;
        this.email = email;
        this.profileImage = profileImage;
        this.memberRole = memberRole;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (!(o instanceof final Member member)) return false;
        return Objects.equals(memberId, member.memberId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(memberId);
    }


    public void updateMemberSetting(final String nickname, final String encodePassword, final String phoneNumber, final String profileImage) {
        changeNickname(nickname);
        password = encodePassword;
        changePhoneNumber(phoneNumber);
        changeProfileImage(profileImage);
    }
}