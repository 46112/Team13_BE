package com.theocean.fundering.global.jwt.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.theocean.fundering.domain.member.repository.MemberRepository;
import com.theocean.fundering.domain.member.domain.Member;
import com.theocean.fundering.global.errors.exception.Exception403;
import com.theocean.fundering.global.jwt.JwtProvider;
import com.theocean.fundering.global.utils.PasswordUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.mapping.GrantedAuthoritiesMapper;
import org.springframework.security.core.authority.mapping.NullAuthoritiesMapper;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import java.io.IOException;

@Slf4j
public class JwtAuthenticationFilter extends BasicAuthenticationFilter {
    private static final String NO_CHECK_URL = "/login";
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
        // "/login" 요청은 토큰 확인 x
        if (request.getRequestURI().equals(NO_CHECK_URL)) {
            chain.doFilter(request, response);
            return;
        }

        final String refreshToken = jwtProvider.extractRefreshToken(request).orElse(null);
        try{
            // 원래 리소스 접근 시 refreshToken 없고 accessToken만 존재
            // accessToken 으로 이메일 비교 후 성공하면 인증 성공, 실패하면 다음 필터에서 인증 오류
            if (null == refreshToken) {
                checkAccessTokenAndAuthentication(request, response, chain);
            }
            // refreshToken 있으면 AccessToken 재발급 하기 위한 것!
            // refreshToken DB에서 비교 후 성공하면 accessToken refreshToken 재발급, but 인증은 안됨
            else {
                checkRefreshTokenAndReIssueAccessToken(response, refreshToken);
            }
            // 두 토큰 다 실패하면 다음 필터에서 403 에러
        }catch(final Exception e){
            forbidden(response, new Exception403("권한이 없습니다."));
        }
    }

    private void checkAccessTokenAndAuthentication(final HttpServletRequest request, final HttpServletResponse response,
                                                   final FilterChain filterChain) throws ServletException, IOException {
        jwtProvider.extractAccessToken(request)
                .filter(jwtProvider::isAccessTokenValid)
                .ifPresent(accessToken -> jwtProvider.verifyAccessTokenAndExtractEmail(accessToken)
                        .ifPresent(email -> memberRepository.findByEmail(email)
                                .ifPresent(this::saveAuthentication)));

        filterChain.doFilter(request, response);
    }

    private void checkRefreshTokenAndReIssueAccessToken(final HttpServletResponse response, final String refreshToken) {
        memberRepository.findByRefreshToken(refreshToken)
                .ifPresent(member -> {
                    final String reIssuedRefreshToken = reIssueRefreshToken(member);
                    jwtProvider.sendAccessAndRefreshToken(
                            response,
                            jwtProvider.createAccessToken(member.getEmail()),
                            reIssuedRefreshToken);
                });
    }

    private String reIssueRefreshToken(final Member member) {
        final String reIssuedRefreshToken = jwtProvider.createRefreshToken(member.getEmail());
        member.updateRefreshToken(reIssuedRefreshToken);
        memberRepository.saveAndFlush(member);
        return reIssuedRefreshToken;
    }

    private void saveAuthentication(final Member myUser) {
        String password = myUser.getPassword();
        if (null == password) { // 소셜 로그인 유저의 비밀번호 임의로 설정 하여 소셜 로그인 유저도 인증 되도록 설정
            password = PasswordUtil.generateRandomPassword();
        }

        final UserDetails userDetailsUser = org.springframework.security.core.userdetails.User.builder()
                .username(myUser.getEmail())
                .password(password)
                .roles(myUser.getUserRole().getType())
                .build();

        final Authentication authentication =
                new UsernamePasswordAuthenticationToken(userDetailsUser, null,
                        authoritiesMapper.mapAuthorities(userDetailsUser.getAuthorities()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    private void forbidden(final HttpServletResponse resp, final Exception403 e) throws IOException {
        resp.setStatus(e.status().value());
        resp.setContentType("application/json; charset=utf-8");
        final ObjectMapper om = new ObjectMapper();
        final String responseBody = om.writeValueAsString(e.body());
        resp.getWriter().println(responseBody);
    }
}
