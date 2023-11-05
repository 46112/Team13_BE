package com.theocean.fundering.domain.member.controller;

import com.theocean.fundering.domain.member.dto.EmailRequestDTO;
import com.theocean.fundering.domain.member.dto.MemberSettingRequestDTO;
import com.theocean.fundering.domain.member.dto.MemberSignUpRequestDTO;
import com.theocean.fundering.domain.member.service.MemberService;
import com.theocean.fundering.global.jwt.userInfo.CustomUserDetails;
import com.theocean.fundering.global.utils.ApiResult;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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
    public ResponseEntity<?> signUp(@RequestBody @Valid final MemberSignUpRequestDTO requestDTO, Error error){
        memberService.signUp(requestDTO);
        return ResponseEntity.ok().body(ApiUtils.success(null));
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping("/user/setting")
    public ResponseEntity<?> findAllUserSetting(
            @AuthenticationPrincipal final CustomUserDetails userDetails
    ){
        var responseDTO = memberService.findAllUserSetting(userDetails.getId());
        return ResponseEntity.ok().body(ApiUtils.success(responseDTO));
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @PutMapping("/user/setting")
    public ResponseEntity<?> updateUserSetting(
            @RequestBody @Valid final MemberSettingRequestDTO requestDTO, Error error,
            @RequestPart(value = "thumbnail") MultipartFile thumbnail,
            @AuthenticationPrincipal final CustomUserDetails userDetails
    ){
        memberService.updateUserSetting(requestDTO, userDetails.getId(), thumbnail);
        return ResponseEntity.ok().body(ApiUtils.success("성공적으로 변경되었습니다."));
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @DeleteMapping("/user/setting/cancellation")
    public ResponseEntity<?> cancellationUser(
            @AuthenticationPrincipal final CustomUserDetails userDetails
    ){
        memberService.cancellationUser(userDetails.getId());
        return ResponseEntity.ok().body(ApiUtils.success("회원 탈퇴 되었습니다."));
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping("/member")
    public ResponseEntity<?> member(){
        return ResponseEntity.ok().body(ApiUtils.success(null));
    }

}