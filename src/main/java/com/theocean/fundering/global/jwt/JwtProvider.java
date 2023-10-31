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
    private static final Long ACCESS_EXP = 1000L * 60 * 60; // 1시간
    private static final Long REFRESH_EXP = 1000L * 60 * 60 * 24 * 14; // 2주

    private static final String EMAIL_CLAIM = "email";
    private static final String TOKEN_PREFIX = "Bearer ";
    public static final String ACCESS_HEADER = "Authorization";
    public static final String REFRESH_HEADER = "Authorization-refresh";

    private static final String ACCESS_SECRET = "MyAccessSecretKey1234";
    private static final String REFRESH_SECRET = "MyRefreshSecretKey1234";

    private final MemberRepository memberRepository;


    public String createAccessToken(final String email) {
        final String jwt = JWT.create()
                .withSubject("AccessToken")
                .withExpiresAt(new Date(System.currentTimeMillis() + ACCESS_EXP))
                .withClaim(EMAIL_CLAIM, email)
                .sign(Algorithm.HMAC512(ACCESS_SECRET));
        return TOKEN_PREFIX + jwt;
    }

    public String createRefreshToken(final String email) {
        final String jwt = JWT.create()
                .withSubject("AccessToken")
                .withExpiresAt(new Date(System.currentTimeMillis() + REFRESH_EXP))
                .withClaim(EMAIL_CLAIM, email)
                .sign(Algorithm.HMAC512(REFRESH_SECRET));
        return TOKEN_PREFIX + jwt;
    }

    public Optional<String> extractAccessToken(final HttpServletRequest request) {
        return Optional.ofNullable(request.getHeader(ACCESS_HEADER))
                .filter(accessToken -> accessToken.startsWith(TOKEN_PREFIX))
                .map(accessToken -> accessToken.replace(TOKEN_PREFIX, ""));
    }

    public Optional<String> extractRefreshToken(final HttpServletRequest request) {
        return Optional.ofNullable(request.getHeader(REFRESH_HEADER))
                .filter(refreshToken -> refreshToken.startsWith(TOKEN_PREFIX))
                .map(refreshToken -> refreshToken.replace(TOKEN_PREFIX, ""));
    }

    public void sendAccessAndRefreshToken(final HttpServletResponse response, final String accessToken, final String refreshToken) {
        response.setStatus(HttpServletResponse.SC_OK);

        response.setHeader(ACCESS_HEADER, accessToken);
        response.setHeader(REFRESH_HEADER, refreshToken);
    }


    public boolean isAccessTokenValid(final String token) {
        try {
            JWT.require(Algorithm.HMAC512(ACCESS_SECRET)).build().verify(token);
            return true;
        } catch (final Exception e) {
            log.error("유효하지 않은 토큰입니다. {}", new String[]{e.getMessage()});
            return false;
        }
    }

    public Optional<String> verifyAccessTokenAndExtractEmail(final String accessToken) {
        try {
            return Optional.ofNullable(JWT.require(Algorithm.HMAC512(ACCESS_SECRET))
                    .build()
                    .verify(accessToken) // accessToken 검증
                    .getClaim(EMAIL_CLAIM) // claim(Email) 가져오기
                    .asString());
        } catch (final Exception e) {
            log.error("액세스 토큰이 유효하지 않습니다.");
            return Optional.empty();
        }
    }
}
