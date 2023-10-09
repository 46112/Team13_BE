package com.theocean.fundering.domain.member.service;

import com.theocean.fundering.domain.member.repository.MemberRepository;
import com.theocean.fundering.domain.member.dto.MemberRequestDTO;
import com.theocean.fundering.global.errors.exception.Exception500;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class MemberService {
    private final PasswordEncoder passwordEncoder;
    private final MemberRepository memberRepository;

    @Transactional
    public void signUp(MemberRequestDTO requestDTO) {
        requestDTO.encodePassword(passwordEncoder.encode(requestDTO.getPassword()));
        try {
            memberRepository.save(requestDTO.getEntity());
        } catch (Exception e) {
            throw new Exception500("회원가입 실패");
        }
    }

    public void login(MemberRequestDTO requestDTO) {
    }
}
