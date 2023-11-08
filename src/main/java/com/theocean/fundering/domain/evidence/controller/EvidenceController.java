package com.theocean.fundering.domain.evidence.controller;

import com.theocean.fundering.domain.evidence.service.EvidenceService;
import com.theocean.fundering.global.jwt.userInfo.CustomUserDetails;
import com.theocean.fundering.global.utils.ApiUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
public class EvidenceController {

    private final EvidenceService evidenceService;

    // (기능) 펀딩 증빙 이미지 업로드
    @PostMapping("/posts/{postId}/withdrawals/{withdrawalId}")
    public ResponseEntity<?> evidenceUpload(
            @AuthenticationPrincipal final CustomUserDetails userDetails,
            @RequestPart(value = "image") MultipartFile img,
            @PathVariable final long postId,
            @PathVariable final long withdrawalId) {

        final Long memberId = 1L; // Long memberId = userDetails.getId();
        final String result = evidenceService.uploadEvidence(memberId, postId, withdrawalId, img);

        return ResponseEntity.ok(ApiUtils.success(result));
    }
}
