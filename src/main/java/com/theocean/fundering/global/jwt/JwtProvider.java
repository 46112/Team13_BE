package com.theocean.fundering.global.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.theocean.fundering.domain.member.repository.MemberRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Optional;

import static org.hibernate.query.sqm.tree.SqmNode.log;

@RequiredArgsConstructor
@Component
public class JwtProvider {
    public static final Long ACCESS_EXP = 1000L * 60 * 60; // 1시간
    public static final Long REFRESH_EXP = 1000L * 60 * 60 * 24 * 14; // 2주

    private static final String EMAIL_CLAIM = "email";
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String ACCESS_HEADER = "Authorization";
    public static final String REFRESH_HEADER = "Authorization-refresh";

    public static final String ACCESS_SECRET = "MyAccessSecretKey1234";
    public static final String REFRESH_SECRET = "MyRefreshSecretKey1234";

    private final MemberRepository memberRepository;


    public String createAccessToken(String email) {
        String jwt = JWT.create()
                .withSubject("AccessToken")
                .withExpiresAt(new Date(System.currentTimeMillis() + ACCESS_EXP))
                .withClaim(EMAIL_CLAIM, email)
                .sign(Algorithm.HMAC512(ACCESS_SECRET));
        return TOKEN_PREFIX + jwt;
    }

    public String createRefreshToken(String email) {
        String jwt = JWT.create()
                .withSubject("AccessToken")
                .withExpiresAt(new Date(System.currentTimeMillis() + REFRESH_EXP))
                .withClaim(EMAIL_CLAIM, email)
                .sign(Algorithm.HMAC512(REFRESH_SECRET));
        return TOKEN_PREFIX + jwt;
    }

    public Optional<String> extractAccessToken(HttpServletRequest request) {
        return Optional.ofNullable(request.getHeader(ACCESS_HEADER))
                .filter(accessToken -> accessToken.startsWith(TOKEN_PREFIX))
                .map(accessToken -> accessToken.replace(TOKEN_PREFIX, ""));
    }

    public Optional<String> extractRefreshToken(HttpServletRequest request) {
        return Optional.ofNullable(request.getHeader(REFRESH_HEADER))
                .filter(refreshToken -> refreshToken.startsWith(TOKEN_PREFIX))
                .map(refreshToken -> refreshToken.replace(TOKEN_PREFIX, ""));
    }

    public void sendAccessAndRefreshToken(HttpServletResponse response, String accessToken, String refreshToken) {
        response.setStatus(HttpServletResponse.SC_OK);

        response.setHeader(ACCESS_HEADER, accessToken);
        response.setHeader(REFRESH_HEADER, refreshToken);
    }


    public boolean isAccessTokenValid(String token) {
        try {
            JWT.require(Algorithm.HMAC512(ACCESS_SECRET)).build().verify(token);
            return true;
        } catch (Exception e) {
            log.error("유효하지 않은 토큰입니다. {}", new String[]{e.getMessage()});
            return false;
        }
    }

    public Optional<String> verifyAccessTokenAndExtractEmail(String accessToken) {
        try {
            return Optional.ofNullable(JWT.require(Algorithm.HMAC512(ACCESS_SECRET))
                    .build()
                    .verify(accessToken) // accessToken 검증
                    .getClaim(EMAIL_CLAIM) // claim(Email) 가져오기
                    .asString());
        } catch (Exception e) {
            log.error("액세스 토큰이 유효하지 않습니다.");
            return Optional.empty();
        }
    }
    public void updateRefreshToken(String email, String refreshToken) {
        memberRepository.findByEmail(email)
                .ifPresentOrElse(
                        user -> user.updateRefreshToken(refreshToken),
                        () -> new Exception("일치하는 회원이 없습니다.")
                );
    }
}
