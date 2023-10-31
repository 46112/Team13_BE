package com.theocean.fundering.domain.member.controller;

import com.theocean.fundering.domain.member.dto.EmailRequestDTO;
import com.theocean.fundering.domain.member.service.MemberService;
import com.theocean.fundering.domain.member.dto.MemberRequestDTO;
import com.theocean.fundering.global.utils.ApiUtils;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class MemberController {
    private final MemberService memberService;

    @PostMapping("/signup/check")
    public ResponseEntity<?> checkEmail(@RequestBody @Valid final EmailRequestDTO emailRequestDTO , Error error){
        memberService.sameCheckEmail(emailRequestDTO.getEmail());
        return ResponseEntity.ok(ApiUtils.success(null));
    }

    @PostMapping("/signup")
    public ResponseEntity<?> signUp(@RequestBody @Valid final MemberRequestDTO requestDTO, Error error){
        memberService.signUp(requestDTO);
        return ResponseEntity.ok().body(ApiUtils.success(null));
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping("/member")
    public ResponseEntity<?> member(){
        return ResponseEntity.ok().body(ApiUtils.success(null));
    }

//    @PostMapping("/login")
//    public ResponseEntity<?> login(@RequestBody @Valid MemberRequestDTO requestDTO, Error error){
//        memberService.login(requestDTO);
//    }
}