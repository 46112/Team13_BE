package com.theocean.fundering.domain.myfunding.controller;

import com.theocean.fundering.domain.myfunding.dto.MyFundingFollowingCelebsDTO;
import com.theocean.fundering.domain.myfunding.dto.MyFundingHostResponseDTO;
import com.theocean.fundering.domain.myfunding.dto.MyFundingManagerResponseDTO;
import com.theocean.fundering.domain.myfunding.dto.MyFundingSupporterResponseDTO;
import com.theocean.fundering.domain.myfunding.service.MyFundingService;
import com.theocean.fundering.global.dto.PageResponse;
import com.theocean.fundering.global.jwt.userInfo.CustomUserDetails;
import com.theocean.fundering.global.utils.ApiUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class MyFundingController {
    private final MyFundingService myFundingService;

    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping("/myfunding/host")
    public ResponseEntity<?> findAllPostingByHost(
            @AuthenticationPrincipal final CustomUserDetails userDetails,
            @RequestParam final Long postId,
            @PageableDefault final Pageable pageable){
        final PageResponse<MyFundingHostResponseDTO> page = myFundingService.findAllPostingByHost(userDetails.getId(), postId, pageable);
        return ResponseEntity.ok(ApiUtils.success(page));
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping("/myfunding/support")
    public ResponseEntity<?> findAllPostingBySupport(
            @AuthenticationPrincipal final CustomUserDetails userDetails,
            @RequestParam final Long postId,
            @PageableDefault final Pageable pageable
    ){
        final PageResponse<MyFundingSupporterResponseDTO> page = myFundingService.findAllPostingBySupporter(userDetails.getId(), postId, pageable);
        return ResponseEntity.ok(ApiUtils.success(page));
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping("/myfunding/nickname")
    public ResponseEntity<?> getNickname(
            @AuthenticationPrincipal final CustomUserDetails userDetails
    ){
        String nickname = myFundingService.getNickname(userDetails.getId());
        return ResponseEntity.ok(ApiUtils.success(nickname));
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping("/myfunding/followers")
    public ResponseEntity<?> findFollowingCelebs(
            @AuthenticationPrincipal final CustomUserDetails userDetails
    ){
        var followingCelebs = myFundingService.findFollowingCelebs(userDetails.getId());
        return ResponseEntity.ok(ApiUtils.success(followingCelebs));
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping("/myfunding/withdrawal")
    public ResponseEntity<?> findAllPostingByManager(
            @AuthenticationPrincipal final CustomUserDetails userDetails,
            @RequestParam final Long postId,
            @PageableDefault final Pageable pageable
    ){
        final PageResponse<MyFundingManagerResponseDTO> page = myFundingService.findAllPostingByManager(userDetails.getId(), postId, pageable);
        return ResponseEntity.ok(ApiUtils.success(null));
    }

    @PostMapping("/myfunding/withdrawal/apply")
    public ResponseEntity<?> applyWithdrawal(
            @AuthenticationPrincipal final CustomUserDetails userDetails,
            @RequestParam final Long postId
    ){
        myFundingService.applyWithdrawal(userDetails.getId(), postId);
        return ResponseEntity.ok(ApiUtils.success(null));
    }
}