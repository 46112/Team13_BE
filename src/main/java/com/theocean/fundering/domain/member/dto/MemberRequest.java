package com.theocean.fundering.domain.member.dto;

import com.theocean.fundering.domain.member.domain.Member;
import com.theocean.fundering.domain.member.domain.constant.MemberRole;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

public class MemberRequest {

    @Getter
    @RequiredArgsConstructor
    public static class EmailRequestDTO {
        @NotEmpty
        @Pattern(regexp = "^[\\w._%+-]+@[\\w.-]+\\.[a-zA-Z]{2,6}$", message = "이메일 형식으로 작성해주세요")
        private String email;

        private EmailRequestDTO(final String email) {
            this.email = email;
        }

        public static EmailRequestDTO from(final String email) {
            return new EmailRequestDTO(email);
        }
    }
    @Getter
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static class SettingDTO {
        private String nickname;
        private String phoneNumber;

        @Size(min = 8, max = 20, message = "8에서 20자 이내여야 합니다.")
        @Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*\\d)(?=.*[@#$%^&+=!~`<>,./?;:'\"\\[\\]{}\\\\()|_-])\\S*$", message = "영문, 숫자, 특수문자가 포함되어야하고 공백이 포함될 수 없습니다.")
        private String currentPassword;

        @Size(min = 8, max = 20, message = "8에서 20자 이내여야 합니다.")
        @Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*\\d)(?=.*[@#$%^&+=!~`<>,./?;:'\"\\[\\]{}\\\\()|_-])\\S*$", message = "영문, 숫자, 특수문자가 포함되어야하고 공백이 포함될 수 없습니다.")
        private String modifyPassword;
    }

    @Getter
    @AllArgsConstructor(access = AccessLevel.PRIVATE)// 테스트 에러 방지
    @NoArgsConstructor(access = AccessLevel.PRIVATE)// 테스트 에러 방지
    public static class SignUpDTO {
        @NotEmpty
        @Pattern(regexp = "^[\\w._%+-]+@[\\w.-]+\\.[a-zA-Z]{2,6}$", message = "이메일 형식으로 작성해주세요")
        private String email;

        @NotEmpty
        @Size(min = 8, max = 20, message = "8에서 20자 이내여야 합니다.")
        @Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*\\d)(?=.*[@#$%^&+=!~`<>,./?;:'\"\\[\\]{}\\\\()|_-])\\S*$", message = "영문, 숫자, 특수문자가 포함되어야하고 공백이 포함될 수 없습니다.")
        private String password;

        @Size(min = 3, max = 45, message = "3에서 45자 이내여야 합니다.")
        @NotEmpty
        private String nickname;

        public static SignUpDTO of(final String email, final String password, final String nickname){
            return new SignUpDTO(email, password, nickname);
        }

        public Member mapToEntity(){
            return Member.builder()
                    .email(email)
                    .nickname(nickname)
                    .password(password)
                    .memberRole(MemberRole.USER)
                    .build();
        }
        public void encodePassword(final String encodePassword){
            password = encodePassword;
        }
    }
}
