package com.theocean.fundering.global.oauth2;

import com.theocean.fundering.domain.member.domain.constant.UserRole;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;

import java.util.Collection;
import java.util.Map;

@Getter
public class CustomOAuth2User extends DefaultOAuth2User {

    private String email;
    private UserRole role;

    public CustomOAuth2User(Collection<? extends GrantedAuthority> authorities,
                            Map<String, Object> attributes, String nameAttributeKey,
                            String email, UserRole userRole) {
        super(authorities, attributes, nameAttributeKey);
        this.email = email;
        this.role = userRole;
    }
}