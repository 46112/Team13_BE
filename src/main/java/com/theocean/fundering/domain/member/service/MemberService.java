package com.theocean.fundering.domain.member.service;

import com.theocean.fundering.domain.member.domain.Member;
import com.theocean.fundering.domain.member.dto.MemberSignUpRequestDTO;
import com.theocean.fundering.domain.member.dto.MemberSettingRequestDTO;
import com.theocean.fundering.domain.member.dto.MemberSettingResponseDTO;
import com.theocean.fundering.domain.member.repository.MemberRepository;
import com.theocean.fundering.global.errors.exception.Exception400;
import com.theocean.fundering.global.errors.exception.Exception500;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class MemberService {
    private final PasswordEncoder passwordEncoder;
    private final MemberRepository memberRepository;

    @Transactional
    public void signUp(final MemberSignUpRequestDTO requestDTO) {
        sameCheckEmail(requestDTO.getEmail());
        requestDTO.encodePassword(passwordEncoder.encode(requestDTO.getPassword()));
        try {
            memberRepository.save(requestDTO.mapToEntity());
        } catch (final Exception e) {
            throw new Exception500("회원가입 실패");
        }
    }

    public void sameCheckEmail(final String email) {
        memberRepository.findByEmail(email).ifPresent(n -> {
                    throw new Exception400("이메일이 존재합니다.");
                });
    }

    public MemberSettingResponseDTO findAllUserSetting(final Long id) {
        final Member member = memberRepository.findById(id).orElseThrow(
                () -> new Exception400("회원을 찾을 수 없습니다.")
        );
        return MemberSettingResponseDTO.from(member);
    }

    @Transactional
    public void modifyUserSetting(@Valid final MemberSettingRequestDTO requestDTO, final Long id) {
        final Member member = memberRepository.findById(id).orElseThrow(
                () -> new Exception400("회원을 찾을 수 없습니다.")
        );
        member.changeNickname(requestDTO.getNickname());
        member.setPassword(passwordEncoder.encode(requestDTO.getModifyPassword()));
        member.changePhoneNumber(requestDTO.getPhoneNumber());
        member.changeProfileImage(requestDTO.getProfileImage());
        try{
            memberRepository.save(member);
        } catch (final Exception e) {
            throw new Exception500("회원 정보 수정에 실패했습니다.");
        }

    }

    @Transactional
    public void cancellationUser(Long userId) {
        final Member member = memberRepository.findById(userId).orElseThrow(
                () -> new Exception400("회원을 찾을 수 없습니다.")
        );
        try{
            memberRepository.delete(member);
        } catch (final Exception e) {
            throw new Exception500("회원 탈퇴에 실패했습니다.");
        }
    }
}
