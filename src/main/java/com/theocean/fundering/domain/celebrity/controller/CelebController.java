package com.theocean.fundering.domain.celebrity.controller;

import com.theocean.fundering.domain.celebrity.dto.*;
import com.theocean.fundering.domain.celebrity.service.CelebService;
import com.theocean.fundering.global.utils.ApiUtils;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
public class CelebController {
    private final CelebService celebService;
    @PostMapping("/celebs")
    public ResponseEntity<?> registerCeleb(@RequestBody @Valid final CelebRequestDTO celebRequestDTO, final Error error){
        celebService.register(celebRequestDTO);
        return ResponseEntity.ok(ApiUtils.success(null));
    }
    @GetMapping("/celebs/{celebId}/posts")
    public ResponseEntity<?> findAllPosting(@PathVariable final Long celebId,
                                            @RequestParam final Long postId,
                                            @PageableDefault(direction = Sort.Direction.DESC) final Pageable pageable){
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
                                            @PageableDefault(direction = Sort.Direction.DESC) final Pageable pageable){
        final PageResponse<CelebListResponseDTO> page = celebService.findAllCeleb(celebId, pageable);
        return ResponseEntity.ok(ApiUtils.success(page));
    }
}
