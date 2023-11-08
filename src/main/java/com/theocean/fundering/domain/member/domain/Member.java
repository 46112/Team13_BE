package com.theocean.fundering.domain.member.domain;

import com.theocean.fundering.global.utils.AuditingFields;
import com.theocean.fundering.domain.member.domain.constant.UserRole;
import jakarta.persistence.*;
import lombok.*;

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
    private Long userId;

    @Setter
    @Column(nullable = false, length = 15)
    private String nickname;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false, length = 50)
    private String email;

    @Column(length = 11)
    private int phoneNumber;

    @Enumerated(EnumType.STRING)
    private UserRole userRole;

    private String socialId; // 로그인한 소셜 타입의 식별자 값 (일반 로그인인 경우 null)

    private String refreshToken; // 리프레시 토큰

    private String profileImage; // 프로필 이미지

    public void changeNickname(final String nickname){
        this.nickname = nickname;
    }

    public void setPassword(final String password){
        this.password = password;
    }

    public void updateRefreshToken(final String updateRefreshToken) {
        refreshToken = updateRefreshToken;
    }

    public void changePhoneNumber(final int phoneNumber){
        this.phoneNumber = phoneNumber;
    }


    public void changeProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }

    @Builder
    public Member(final Long userId, final String nickname, final String password, final String email, final UserRole userRole, final String profileImage) {
        this.userId = userId;
        this.nickname = nickname;
        this.password = password;
        this.email = email;
        this.profileImage = profileImage;
        this.userRole = userRole;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (!(o instanceof final Member member)) return false;
        return Objects.equals(userId, member.userId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId);
    }


}

