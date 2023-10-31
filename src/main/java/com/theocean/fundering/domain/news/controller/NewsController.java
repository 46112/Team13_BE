package com.theocean.fundering.domain.news.controller;

import com.theocean.fundering.domain.news.dto.NewsRequest;
import com.theocean.fundering.domain.news.dto.NewsResponse;
import com.theocean.fundering.domain.news.service.CreateNewsService;
import com.theocean.fundering.domain.news.service.ReadNewsService;
import com.theocean.fundering.global.jwt.userInfo.CustomUserDetails;
import com.theocean.fundering.global.utils.ApiUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class NewsController {

    private final CreateNewsService createNewsService;
    private final ReadNewsService readNewsService;

    // (기능) 펀딩 업데이트 작성
    @PostMapping("/posts/{postId}/updates")
    public ResponseEntity<?> createUpdates(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable long postId,
            @RequestBody NewsRequest.saveDTO request) {

        // TODO: 추후 리팩토링 예정
        Long writerId = 1L;
        createNewsService.createNews(writerId, postId, request);

        return ResponseEntity.ok(ApiUtils.success(null));
    }

    // (기능) 펀딩 업데이트 조회
    @GetMapping("/posts/{postId}/updates")
    public ResponseEntity<?> readUpdates(
            @PathVariable long postId,
            @RequestParam(required = false, defaultValue = "0") long cursor,
            @RequestParam(required = false, defaultValue = "6") int pageSize) {

        NewsResponse.findAllDTO response = readNewsService.getNews(postId, cursor, pageSize);

        return ResponseEntity.ok(ApiUtils.success(response));
    }
}
