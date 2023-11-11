package com.theocean.fundering.global.jwt.userInfo;

import com.theocean.fundering.domain.member.domain.Member;
import com.theocean.fundering.domain.member.domain.constant.MemberRole;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Getter
@RequiredArgsConstructor
@Builder
@AllArgsConstructor
public class CustomUserDetails implements UserDetails {

    private Long id;
    private String email;
    private String password;
    private MemberRole role;

    public static CustomUserDetails from(final Member member) {
        return builder()
                .id(member.getMemberId())
                .email(member.getEmail())
                .password(member.getPassword())
                .role(member.getMemberRole())
                .build();
    }

    public Long getId() {
        return id;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.getType()));
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
