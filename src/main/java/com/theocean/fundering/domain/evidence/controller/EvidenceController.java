package com.theocean.fundering.domain.evidence.controller;

import com.theocean.fundering.domain.evidence.service.EvidenceService;
import com.theocean.fundering.global.jwt.userInfo.CustomUserDetails;
import com.theocean.fundering.global.utils.ApiResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Encoding;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Tag(name = "EVIDENCE", description = "증빙 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class EvidenceController {

    private final EvidenceService evidenceService;

    // (기능) 펀딩 증빙 이미지 업로드
    @Operation(
            summary = "출금 증빙 이미지 업로드",
            description = "펀딩 id와 출금 id를 기반으로 출금 내역에 출금 증빙 이미지를 추가한다.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    content = @Content(
                            mediaType = MediaType.MULTIPART_FORM_DATA_VALUE,
                            schema = @Schema(type = "string", format = "binary"),
                            encoding = @Encoding(name = "image", contentType = "image/png")
                    )
            )
    )
    @PostMapping(value = "/posts/{postId}/withdrawals/{withdrawalId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public ApiResult<String> uploadEvidence(
            @AuthenticationPrincipal final CustomUserDetails userDetails,
            @Parameter(description = "이미지 파일", required = true) @RequestPart("image") final MultipartFile image,
            @Parameter(description = "게시글의 PK") @PathVariable final long postId,
            @Parameter(description = "출금 PK") @PathVariable final long withdrawalId
    ){
        final Long memberId = userDetails.getId();
        final String result = evidenceService.uploadEvidence(memberId, postId, withdrawalId, image);

        return ApiResult.success(result);
    }
}
