package com.theocean.fundering.domain.member.dto;

import com.theocean.fundering.domain.member.domain.Member;
import com.theocean.fundering.domain.member.domain.constant.UserRole;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
public class MemberRequestDTO {
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

    public static MemberRequestDTO from(Member member){
        return MemberRequestDTO.builder()
                .email(member.getEmail())
                .nickname(member.getNickname())
                .password(member.getPassword())
                .build();
    }
    public Member getEntity(){
        return Member.builder()
                .email(email)
                .nickname(nickname)
                .password(password)
                .userRole(UserRole.USER)
                .build();
    }
    public void encodePassword(String encodePassword){
        this.password = encodePassword;
    }

}
