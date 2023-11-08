package com.theocean.fundering.domain.member.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class EmailRequestDTO {
    @NotEmpty
    @Pattern(regexp = "^[\\w._%+-]+@[\\w.-]+\\.[a-zA-Z]{2,6}$", message = "이메일 형식으로 작성해주세요")
    private String email;

    private EmailRequestDTO(final String email) {
        this.email = email;
    }

    public static EmailRequestDTO from(final String email){
        return new EmailRequestDTO(email);
    }
}
