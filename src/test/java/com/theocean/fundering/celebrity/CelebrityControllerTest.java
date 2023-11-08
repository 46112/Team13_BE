package com.theocean.fundering.celebrity;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.theocean.fundering.domain.celebrity.controller.CelebController;
import com.theocean.fundering.domain.celebrity.domain.Celebrity;
import com.theocean.fundering.domain.celebrity.domain.constant.CelebGender;
import com.theocean.fundering.domain.celebrity.domain.constant.CelebType;
import com.theocean.fundering.domain.celebrity.dto.CelebRequestDTO;
import com.theocean.fundering.domain.celebrity.service.CelebService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.security.oauth2.client.servlet.OAuth2ClientAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CelebController.class)
@ImportAutoConfiguration(exclude = OAuth2ClientAutoConfiguration.class)
class CelebrityControllerTest {
    private static final String CELEB_NAME = "아이유";
    private static final String PROFILE_IMAGE = "profile.jpg";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper om;

    @MockBean
    private CelebService celebService;

    private final Celebrity celeb = Celebrity.builder()
            .celebName(CELEB_NAME)
            .celebGender(CelebGender.FEMALE)
            .celebType(CelebType.SINGER)
            .profileImage(PROFILE_IMAGE)
            .build();

    @WithMockUser
    @Test
    void reigsterCeleb() throws Exception {
        //given
        final CelebRequestDTO requestDTO = CelebRequestDTO.builder()
                .celebName(celeb.getCelebName())
                .celebGender(celeb.getCelebGender())
                .celebType(celeb.getCelebType())
                .profileImage(celeb.getProfileImage())
                .build();
        final String requestBody = om.writeValueAsString(requestDTO);

        //when
        final ResultActions result = mockMvc.perform(
                post("/celebs")
                        .content(requestBody)
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(csrf())
        );

        //then
        result.andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.success").value("true"));
    }
}
