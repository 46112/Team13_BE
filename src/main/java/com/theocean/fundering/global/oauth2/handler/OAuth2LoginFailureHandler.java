package com.theocean.fundering.global.oauth2.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.theocean.fundering.global.errors.exception.Exception400;
import com.theocean.fundering.global.errors.exception.Exception401;
import com.theocean.fundering.global.utils.ApiUtils;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class OAuth2LoginFailureHandler implements AuthenticationFailureHandler {
    private final String FAILURE_MESSAGE = "아이디나 비밀번호가 잘못 되었습니다.";
    private final ObjectMapper objectMapper;
    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        log.info("로그인에 실패했습니다. 메시지 : {}", exception.getMessage());
        String result = objectMapper.writeValueAsString(ApiUtils.error(FAILURE_MESSAGE, new Exception401("아이디나 비밀번호가 잘못 되었습니다.").status()));
        response.getWriter().write(result);
    }
}