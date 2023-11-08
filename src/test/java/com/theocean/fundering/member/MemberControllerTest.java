package com.theocean.fundering.member;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.theocean.fundering.domain.member.domain.Member;
import com.theocean.fundering.domain.member.domain.constant.UserRole;
import com.theocean.fundering.domain.member.dto.EmailRequestDTO;
import com.theocean.fundering.domain.member.dto.MemberSignUpRequestDTO;
import com.theocean.fundering.domain.member.repository.MemberRepository;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedHashMap;
import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@MockBean(JpaMetamodelMappingContext.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
class MemberControllerTest {
    private static final String KEY_EMAIL = "email";
    private static final String KEY_PASSWORD = "password";
    private static final String EMAIL = "test@naver.com";
    private static final String PASSWORD = "password1!";
    private static final String WRONG_PASSWORD = "wrongPassword";
    private static final String NICKNAME = "nickname";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper om;

    @Autowired
    private EntityManager em;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private PasswordEncoder delegatingPasswordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();

    @BeforeEach
    void beforeEach() {
        final Member member = Member.builder().email(EMAIL).password(delegatingPasswordEncoder.encode(PASSWORD)).nickname(NICKNAME).userRole(UserRole.USER).build();
        memberRepository.save(member);
        clear();
    }

    private void clear() {
        em.flush();
        em.clear();
    }

    private Map<String, String> getUsernamePasswordMap(final String email, final String password) {
        final Map<String, String> usernamePasswordMap = new LinkedHashMap<>();
        usernamePasswordMap.put(KEY_EMAIL, email);
        usernamePasswordMap.put(KEY_PASSWORD, password);
        return usernamePasswordMap;
    }

    @DisplayName("회원 가입 성공 테스트")
    @Transactional
    @Test
    void join_test() throws Exception {
        //given
        final MemberSignUpRequestDTO requestDTO = MemberSignUpRequestDTO.of("test@kakao.com", "password1!", "boseungk");
        final String requestBody = om.writeValueAsString(requestDTO);

        //when
        final ResultActions result = mockMvc.perform(
                post("/signup")
                        .content(requestBody)
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(csrf())
        );

        //then
        result.andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.success").value("true"));
    }

    @DisplayName("로그인 성공 테스트")
    @Transactional
    @Test
    void login_success_test() throws Exception {
        //given
        final Map<String, String> usernamePasswordMap = getUsernamePasswordMap(EMAIL, PASSWORD);

        //when
        final ResultActions result = mockMvc.perform(
                post("/login")
                        .content(objectMapper.writeValueAsString(usernamePasswordMap))
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(csrf())
        );

        //then
        result.andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.success").value("true"));
    }

    @DisplayName("로그인 실패 테스트")
    @Transactional
    @Test
    void login_fail_test() throws Exception {
        //given
        final Map<String, String> usernamePasswordMap = getUsernamePasswordMap(EMAIL, WRONG_PASSWORD);

        //when
        final ResultActions result = mockMvc.perform(
                post("/login")
                        .content(objectMapper.writeValueAsString(usernamePasswordMap))
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(csrf())
        );

        //then
        result.andExpect(status().isUnauthorized())
                .andExpect(MockMvcResultMatchers.jsonPath("$.success").value("false"));
    }

    @DisplayName("이메일 성공 테스트")
    @Transactional
    @Test
    void email_success_test() throws Exception {
        //given
        final EmailRequestDTO requestDTO = EmailRequestDTO.from("test@kakao.com");

        final String requestBody = om.writeValueAsString(requestDTO);

        //when
        final ResultActions result = mockMvc.perform(
                post("/signup/check")
                        .content(requestBody)
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(csrf())
        );

        //then
        result.andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.success").value("true"));
    }

    @DisplayName("이메일 실패 테스트")
    @Transactional
    @Test
    void email_fail_test() throws Exception {
        //given
        final EmailRequestDTO requestDTO = EmailRequestDTO.from("test@kakaocom");

        final String requestBody = om.writeValueAsString(requestDTO);

        //when
        final ResultActions result = mockMvc.perform(
                post("/signup/check")
                        .content(requestBody)
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(csrf())
        );

        //then
        result.andExpect(status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.success").value("false"));
    }

    @DisplayName("User 권한 접근 성공 테스트")
    @WithMockUser
    @Transactional
    @Test
    void role_success_test() throws Exception{

        //given
        //when
        final ResultActions result = mockMvc.perform(
                get("/member")
                        .with(csrf())
        );
        //then
        result.andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.success").value("true"));
    }

    @DisplayName("User 권한 접근 실패 테스트")
    @Transactional
    @Test
    void role_fail_test() throws Exception{

        //given
        //when
        final ResultActions result = mockMvc.perform(
                get("/member")
                        .with(csrf())
        );
        //then
        result.andExpect(status().isForbidden())
                .andExpect(MockMvcResultMatchers.jsonPath("$.success").value("false"));
    }
}