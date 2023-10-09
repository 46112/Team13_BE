package com.theocean.fundering.global.jwt.filter;

import com.theocean.fundering.domain.member.repository.MemberRepository;
import com.theocean.fundering.domain.member.domain.Member;
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

    private GrantedAuthoritiesMapper authoritiesMapper = new NullAuthoritiesMapper();

    public JwtAuthenticationFilter(AuthenticationManager authenticationManager, MemberRepository memberRepository, JwtProvider jwtProvider) {
        super(authenticationManager);
        this.memberRepository = memberRepository;
        this.jwtProvider = jwtProvider;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        // "/login" 요청은 토큰 확인 x
        if (request.getRequestURI().equals(NO_CHECK_URL)) {
            chain.doFilter(request, response);
            return;
        }

        String refreshToken = jwtProvider.extractRefreshToken(request).orElse(null);

        //refreshToken 존재하면 확인 후 AccessToken 재발행
        if (refreshToken != null) {
            checkRefreshTokenAndReIssueAccessToken(response, refreshToken);
        }
        //refreshToken 없으면 AccessToken 확인 후, 해당 이메일 유저 DB에서 조회해서 인증 객체에 저장
        else {
            checkAccessTokenAndAuthentication(request, response, chain);
        }
        // 두 토큰 다 실패하면 다음 필터에서 403 에러
    }

    public void checkAccessTokenAndAuthentication(HttpServletRequest request, HttpServletResponse response,
                                                  FilterChain filterChain) throws ServletException, IOException {
        jwtProvider.extractAccessToken(request)
                .filter(jwtProvider::isAccessTokenValid)
                .ifPresent(accessToken -> jwtProvider.verifyAccessTokenAndExtractEmail(accessToken)
                        .ifPresent(email -> memberRepository.findByEmail(email)
                                .ifPresent(this::saveAuthentication)));

        filterChain.doFilter(request, response);
    }

    public void checkRefreshTokenAndReIssueAccessToken(HttpServletResponse response, String refreshToken) {
        memberRepository.findByRefreshToken(refreshToken)
                .ifPresent(member -> {
                    String reIssuedRefreshToken = reIssueRefreshToken(member);
                    jwtProvider.sendAccessAndRefreshToken(
                            response,
                            jwtProvider.createAccessToken(member.getEmail()),
                            reIssuedRefreshToken);
                });
    }

    private String reIssueRefreshToken(Member member) {
        String reIssuedRefreshToken = jwtProvider.createRefreshToken(member.getEmail());
        member.updateRefreshToken(reIssuedRefreshToken);
        memberRepository.saveAndFlush(member);
        return reIssuedRefreshToken;
    }

    public void saveAuthentication(Member myUser) {
        String password = myUser.getPassword();
        if (password == null) { // 소셜 로그인 유저의 비밀번호 임의로 설정 하여 소셜 로그인 유저도 인증 되도록 설정
            password = PasswordUtil.generateRandomPassword();
        }

        UserDetails userDetailsUser = org.springframework.security.core.userdetails.User.builder()
                .username(myUser.getEmail())
                .password(password)
                .build();

        Authentication authentication =
                new UsernamePasswordAuthenticationToken(userDetailsUser, null,
                        authoritiesMapper.mapAuthorities(userDetailsUser.getAuthorities()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
    }
}
