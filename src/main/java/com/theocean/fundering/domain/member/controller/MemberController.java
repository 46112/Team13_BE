package com.theocean.fundering.domain.member.controller;

import com.theocean.fundering.domain.member.service.MemberService;
import com.theocean.fundering.domain.member.dto.MemberRequestDTO;
import com.theocean.fundering.global.utils.ApiUtils;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class MemberController {
    private final MemberService memberService;

    @PostMapping("/signup")
    public ResponseEntity<?> signUp(@RequestBody @Valid MemberRequestDTO requestDTO, Error error){
        memberService.signUp(requestDTO);
        return ResponseEntity.ok().body(ApiUtils.success(null));
    }
//    @PostMapping("/login")
//    public ResponseEntity<?> login(@RequestBody @Valid MemberRequestDTO requestDTO, Error error){
//        memberService.login(requestDTO);
//    }
}