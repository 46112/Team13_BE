package com.theocean.fundering.global.jwt.handler;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.theocean.fundering.domain.member.repository.MemberRepository;
import com.theocean.fundering.global.jwt.JwtProvider;
import com.theocean.fundering.global.utils.ApiUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;

import java.io.IOException;


@Slf4j
@RequiredArgsConstructor
public class LoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    private final String SUCCESS_MESSAGE = "로그인에 성공했습니다.";

    private final MemberRepository memberRepository;
    private final ObjectMapper objectMapper;
    private final JwtProvider jwtProvider;


    @Override
    public void onAuthenticationSuccess(final HttpServletRequest request, final HttpServletResponse response,
                                        final Authentication authentication) throws IOException {
        final String email = extractUsername(authentication);
        final String accessToken = jwtProvider.createAccessToken(email);
        final String refreshToken = jwtProvider.createRefreshToken(email);

        jwtProvider.sendAccessAndRefreshToken(response, accessToken, refreshToken);

        memberRepository.findByEmail(email)
                .ifPresent(user -> {
                    user.updateRefreshToken(refreshToken);
                    memberRepository.saveAndFlush(user);
                });
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");
        final String result = objectMapper.writeValueAsString(ApiUtils.success(SUCCESS_MESSAGE));
        response.getWriter().write(result);
    }

    private String extractUsername(final Authentication authentication) {
        final UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        return userDetails.getUsername();
    }
}
