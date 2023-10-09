package com.theocean.fundering.domain.member;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.theocean.fundering.domain.member.domain.Member;
import com.theocean.fundering.domain.member.dto.MemberRequestDTO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("test")
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
class MemberControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper om;

    @DisplayName("회원가입 테스트")
    @Test
    void signup() throws Exception {
        //given
//        Member member = new Member(1L, "보승", "1234", "gda12@naver.com");
//        MemberRequestDTO requestDTO = MemberRequestDTO.from(member);
//
//        String requestBody = om.writeValueAsString(requestDTO);
//
//        // when
//        ResultActions result = mvc.perform(
//                MockMvcRequestBuilders
//                        .post("/signup")
//                        .content(requestBody)
//                        .contentType(MediaType.APPLICATION_JSON)
//        );
//
//        String responseBody = result.andReturn().getResponse().getContentAsString();
//        System.out.println("테스트 : " + responseBody);
//
//        // then
//        result.andExpect(MockMvcResultMatchers.jsonPath("$.success").value("true"));
    }
}