package com.theocean.fundering.global.jwt.handler;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.theocean.fundering.domain.member.domain.Member;
import com.theocean.fundering.domain.member.repository.AdminRepository;
import com.theocean.fundering.domain.member.repository.MemberRepository;
import com.theocean.fundering.global.errors.exception.ErrorCode;
import com.theocean.fundering.global.errors.exception.Exception400;
import com.theocean.fundering.global.jwt.JwtProvider;
import com.theocean.fundering.global.jwt.dto.LoginResponse;
import com.theocean.fundering.global.utils.ApiResult;
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

    private final MemberRepository memberRepository;
    private final AdminRepository adminRepository;
    private final ObjectMapper objectMapper;
    private final JwtProvider jwtProvider;


    @Override
    public void onAuthenticationSuccess(final HttpServletRequest request, final HttpServletResponse response,
                                        final Authentication authentication) throws IOException {
        final String email = extractUsername(authentication);
        final String accessToken = jwtProvider.createAccessToken(email);
        final String refreshToken = jwtProvider.createRefreshToken(email);

        jwtProvider.sendAccess(response, accessToken);

        final Member member = memberRepository.findByEmail(email).orElseThrow(
                () -> new Exception400(ErrorCode.ER01)
        );
        member.updateRefreshToken(refreshToken);
        memberRepository.saveAndFlush(member);

        final boolean isAdmin = adminRepository.findByMemberId(member.getMemberId()).stream().anyMatch(id -> id.equals(member.getMemberId()));
        final var responseDTO = LoginResponse.SuccessDTO.of(member.getProfileImage(), member.getNickname(), isAdmin, refreshToken);
        createResponse(response, responseDTO);
    }

    private String extractUsername(final Authentication authentication) {
        final UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        return userDetails.getUsername();
    }

    private void createResponse(final HttpServletResponse response, final LoginResponse.SuccessDTO responseDTO) throws IOException {
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");
        final String result = objectMapper.writeValueAsString(ApiResult.success(responseDTO));
        response.getWriter().write(result);
    }
}
