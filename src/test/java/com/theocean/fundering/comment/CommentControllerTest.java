package com.theocean.fundering.comment;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.theocean.fundering.domain.comment.controller.CommentController;
import com.theocean.fundering.domain.comment.dto.CommentRequest;
import com.theocean.fundering.domain.comment.service.CreateCommentService;
import com.theocean.fundering.domain.comment.service.DeleteCommentService;
import com.theocean.fundering.domain.comment.service.ReadCommentService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CommentController.class)
@MockBean(JpaMetamodelMappingContext.class)
public class CommentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CreateCommentService createCommentService;

    @MockBean
    private DeleteCommentService deleteCommentService;

    @MockBean
    private ReadCommentService readCommentService;


    @DisplayName("USER role을 지닌 유저는 댓글 생성에 성공합니다.")
    @WithMockUser(roles = {"USER"})  // USER 권한을 가진 사용자로 설정
    @Test
    public void createComment_withUserRole_shouldSucceed() throws Exception {

        this.mockMvc.perform(post("/posts/{postId}/comments", 1L).with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"content\":\"test comment\"}"))
                .andExpect(status().isOk());
    }

    @DisplayName("User role을 지니지 못한 유저는 댓글 생성에 실패합니다.")
    @WithMockUser(roles = "GUEST")  // GUEST 권한을 가진 사용자로 설정
    @Test
    public void createComment_withGuestRole_shouldFail() throws Exception {
        this.mockMvc.perform(post("/posts/{postId}/comments", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"content\":\"test comment\"}"))
                .andExpect(status().isForbidden());  // 403 Forbidden 예상
    }

    @DisplayName("User role을 지닌 유저는 댓글 삭제에 성공합니다.")
    @WithMockUser(roles = "USER")  // USER 권한을 가진 사용자로 설정
    @Test
    public void deleteComment_withUserRole_shouldSucceed() throws Exception {
        this.mockMvc.perform(delete("/posts/{postId}/comments/{commentId}", 1L, 1L).with(csrf()))
                .andExpect(status().isOk());
    }

    @DisplayName("User role을 지니지 못한 유저는 댓글 삭제에 실패합니다.")
    @WithMockUser(roles = "GUEST")  // GUEST 권한을 가진 사용자로 설정
    @Test
    public void deleteComment_withGuestRole_shouldFail() throws Exception {
        this.mockMvc.perform(delete("/posts/{postId}/comments/{commentId}", 1L, 1L))
                .andExpect(status().isForbidden());  // 403 Forbidden 예상
    }

    @DisplayName("유저가 내용이 비어있는 댓글 작성을 시도하면 실패한다.")
    @WithMockUser(roles = "USER")
    @Test
    public void createComment_withEmptyContent_shouldReturn400() throws Exception {
        // Given
        CommentRequest.SaveDTO request = CommentRequest.SaveDTO.builder().content("").build();
        String jsonRequest = new ObjectMapper().writeValueAsString(request);

        // When & Then
        mockMvc.perform(post("/posts/1/comments").with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error.message").value("댓글 내용은 필수입니다."));
    }
}