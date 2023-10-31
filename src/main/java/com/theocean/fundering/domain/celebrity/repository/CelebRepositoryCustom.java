package com.theocean.fundering.domain.celebrity.repository;

import com.theocean.fundering.domain.celebrity.dto.CelebFundingResponseDTO;
import com.theocean.fundering.domain.celebrity.dto.CelebListResponseDTO;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

public interface CelebRepositoryCustom {
    Slice<CelebFundingResponseDTO> findAllPosting(Long celebId, Long postId, Pageable pageable);

    Slice<CelebListResponseDTO> findAllCeleb(Long celebId, Pageable pageable);
}
