package com.theocean.fundering.global.jwt.filter;

import com.theocean.fundering.domain.member.domain.Member;
import com.theocean.fundering.domain.member.repository.MemberRepository;
import com.theocean.fundering.global.jwt.JwtProvider;
import com.theocean.fundering.global.jwt.userInfo.CustomUserDetails;
import com.theocean.fundering.global.utils.PasswordUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.mapping.GrantedAuthoritiesMapper;
import org.springframework.security.core.authority.mapping.NullAuthoritiesMapper;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import java.io.IOException;

@Slf4j
public class JwtAuthenticationFilter extends BasicAuthenticationFilter {
    private static final String NO_CHECK_URL = "/api/login";
    private final MemberRepository memberRepository;
    private final JwtProvider jwtProvider;

    private final GrantedAuthoritiesMapper authoritiesMapper = new NullAuthoritiesMapper();

    public JwtAuthenticationFilter(final AuthenticationManager authenticationManager, final MemberRepository memberRepository, final JwtProvider jwtProvider) {
        super(authenticationManager);
        this.memberRepository = memberRepository;
        this.jwtProvider = jwtProvider;
    }

    @Override
    protected void doFilterInternal(final HttpServletRequest request, final HttpServletResponse response, final FilterChain chain) throws IOException, ServletException {
        // "/api/login" 요청은 토큰 확인 x
        if (request.getRequestURI().equals(NO_CHECK_URL)) {
            chain.doFilter(request, response);
            return;
        }
        // refresh token 만료 시 /token/refresh 에서 요청
        checkAccessTokenAndAuthentication(request, response, chain);

    }

    private void checkAccessTokenAndAuthentication(final HttpServletRequest request, final HttpServletResponse response, final FilterChain filterChain) throws ServletException, IOException {
        jwtProvider.extractAccessToken(request)
                .filter(jwtProvider::isAccessTokenValid)
                .ifPresent(accessToken -> jwtProvider.verifyAccessTokenAndExtractEmail(accessToken)
                        .ifPresent(email -> memberRepository.findByEmail(email)
                                .ifPresent(this::saveAuthentication)));

        filterChain.doFilter(request, response);
    }

    private void saveAuthentication(final Member myUser) {
        String password = myUser.getPassword();
        if (null == password) { // 소셜 로그인 유저의 비밀번호 임의로 설정 하여 소셜 로그인 유저도 인증 되도록 설정
            password = PasswordUtil.generateRandomPassword();
        }

        final UserDetails userDetailsUser = User.builder()
                .username(myUser.getEmail())
                .password(password)
                .roles(myUser.getMemberRole().getType())
                .build();

        final UserDetails customUserDetailsUser = CustomUserDetails.from(myUser);

        final Authentication authentication =
                new UsernamePasswordAuthenticationToken(
                        customUserDetailsUser,
                        customUserDetailsUser.getPassword(),
                        userDetailsUser.getAuthorities()
                );

        SecurityContextHolder.getContext().setAuthentication(authentication);
    }
}
