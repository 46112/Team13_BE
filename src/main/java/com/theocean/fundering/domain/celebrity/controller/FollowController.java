package com.theocean.fundering.domain.celebrity.controller;

import com.theocean.fundering.domain.celebrity.service.FollowService;
import com.theocean.fundering.global.jwt.userInfo.CustomUserDetails;
import com.theocean.fundering.global.utils.ApiUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class FollowController {
    private final FollowService followService;

    @PreAuthorize("hasRole('ROLE_USER')")
    @PostMapping("/celebs/{celebId}/follow")
    public ResponseEntity<?> followCelebs(@PathVariable Long celebId,
                                          @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        followService.followCelebs(celebId, userDetails.getId());
        return ResponseEntity.ok(ApiUtils.success(null));
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @PostMapping("/celebs/{celebId}/unfollow")
    public ResponseEntity<?> unfollowCelebs(@PathVariable Long celebId,
                                            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        followService.unFollowCelebs(celebId, userDetails.getId());
        return ResponseEntity.ok(ApiUtils.success(null));
    }

}
