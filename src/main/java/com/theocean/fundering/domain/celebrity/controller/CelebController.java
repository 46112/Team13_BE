package com.theocean.fundering.domain.celebrity.controller;

import com.theocean.fundering.domain.celebrity.dto.*;
import com.theocean.fundering.domain.celebrity.service.CelebService;
import com.theocean.fundering.global.dto.PageResponse;
import com.theocean.fundering.global.jwt.userInfo.CustomUserDetails;
import com.theocean.fundering.global.utils.ApiUtils;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class CelebController {
    private final CelebService celebService;
    @PostMapping("/celebs")
    public ResponseEntity<?> registerCeleb(@RequestBody @Valid final CelebRequestDTO celebRequestDTO, final Error error){
        celebService.register(celebRequestDTO);
        return ResponseEntity.ok(ApiUtils.success(null));
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/celebs/{celebId}/admin")
    public ResponseEntity<?> findAllCelebForApproval(@PathVariable final Long celebId,
                                                     @PageableDefault final Pageable pageable
    ){
        PageResponse<CelebListResponseDTO> page = celebService.findAllCelebForApproval(celebId, pageable);
        return ResponseEntity.ok(ApiUtils.success(page));
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/celebs/{celebId}/admin")
    public ResponseEntity<?> approvalCelebrity(@PathVariable final Long celebId){
        celebService.approvalCelebrity(celebId);
        return ResponseEntity.ok(ApiUtils.success(null));
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/celebs/{celebId}/admin")
    public ResponseEntity<?> rejectCelebrity(@PathVariable final Long celebId){
        celebService.deleteCelebrity(celebId);
        return ResponseEntity.ok(ApiUtils.success(null));
    }

    @GetMapping("/celebs/{celebId}/posts")
    public ResponseEntity<?> findAllPosting(@PathVariable final Long celebId,
                                            @RequestParam final Long postId,
                                            @PageableDefault final Pageable pageable){
        final PageResponse<CelebFundingResponseDTO> page = celebService.findAllPosting(celebId, postId, pageable);
        return ResponseEntity.ok(ApiUtils.success(page));
    }

    @GetMapping("/celebs/{celebId}")
    public ResponseEntity<?> findByCelebId(@PathVariable final Long celebId){
        final CelebDetailsResponseDTO responseDTO = celebService.findByCelebId(celebId);
        return ResponseEntity.ok(ApiUtils.success(responseDTO));
    }

    @GetMapping("/celebs")
    public ResponseEntity<?> findAllCelebs(@RequestParam final Long celebId,
                                            @RequestParam final String keyword,
                                            @PageableDefault final Pageable pageable){
        final PageResponse<CelebListResponseDTO> page = celebService.findAllCeleb(celebId, keyword, pageable);
        return ResponseEntity.ok(ApiUtils.success(page));
    }

    @GetMapping("/celebs/recommend")
    public ResponseEntity<?> findAllRecommendCelebs(
            @AuthenticationPrincipal CustomUserDetails userDetails
    ){
        List<CelebsRecommendDTO> responseDTO = celebService.recommendCelebs(userDetails);
        return ResponseEntity.ok(ApiUtils.success(responseDTO));
    }

}
