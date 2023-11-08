package com.theocean.fundering.domain.evidence.service;

import com.theocean.fundering.domain.evidence.domain.Evidence;
import com.theocean.fundering.domain.evidence.repository.EvidenceRepository;
import com.theocean.fundering.global.utils.AWSS3Uploader;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Transactional
@RequiredArgsConstructor
@Service
public class EvidenceService {

    private final AWSS3Uploader awss3Uploader;
    private final EvidenceRepository evidenceRepository;

    public String uploadEvidence(final Long memberId, final Long postId, final Long withdrawalId, final MultipartFile img) {
        final String imgUrl = uploadImage(img);

        final Evidence evidence = Evidence.builder()
                .withdrawalId(withdrawalId)
                .applicantId(memberId)
                .postId(postId)
                .url(imgUrl)
                .build();

        evidenceRepository.save(evidence);

        return imgUrl;
    }

    private String uploadImage(final MultipartFile img) {
        return awss3Uploader.uploadToS3(img);
    }
}
