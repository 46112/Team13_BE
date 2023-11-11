package com.theocean.fundering.domain.member.service;

import com.theocean.fundering.domain.member.domain.Member;
import com.theocean.fundering.domain.member.dto.MemberRequest;
import com.theocean.fundering.domain.member.dto.MemberResponse;
import com.theocean.fundering.domain.member.repository.MemberRepository;
import com.theocean.fundering.global.errors.exception.ErrorCode;
import com.theocean.fundering.global.errors.exception.Exception400;
import com.theocean.fundering.global.utils.AWSS3Uploader;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class MemberService {
    private final PasswordEncoder passwordEncoder;
    private final MemberRepository memberRepository;
    private final AWSS3Uploader awss3Uploader;

    @Transactional
    public void signUp(final MemberRequest.SignUpDTO requestDTO) {
        sameCheckEmail(requestDTO.getEmail());
        requestDTO.encodePassword(passwordEncoder.encode(requestDTO.getPassword()));
        memberRepository.save(requestDTO.mapToEntity());
    }

    public void sameCheckEmail(final String email) {
        memberRepository.findByEmail(email).ifPresent(n -> {
            throw new Exception400(ErrorCode.ER16);
        });
    }

    public MemberResponse.SettingDTO findAllMemberSetting(final Long id) {
        final Member member = memberRepository.findById(id).orElseThrow(
                () -> new Exception400(ErrorCode.ER01)
        );
        return MemberResponse.SettingDTO.from(member);
    }

    @Transactional
    public void updateMemberSetting(@Valid final MemberRequest.SettingDTO requestDTO, final Long id, MultipartFile thumbnail) {
        final Member member = memberRepository.findById(id).orElseThrow(
                () -> new Exception400(ErrorCode.ER01)
        );
        final String encodePassword = passwordEncoder.encode(requestDTO.getModifyPassword());
        final String img = uploadImage(thumbnail);
        member.updateMemberSetting(requestDTO.getNickname(), encodePassword, requestDTO.getPhoneNumber(), img);
        memberRepository.save(member);
    }

    @Transactional
    public void cancellationMember(final Long memberId) {
        final Member member = memberRepository.findById(memberId).orElseThrow(
                () -> new Exception400(ErrorCode.ER01)
        );
        memberRepository.delete(member);
    }

    private String uploadImage(final MultipartFile img) {
        return awss3Uploader.uploadToS3(img);
    }
}