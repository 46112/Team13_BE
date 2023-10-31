package com.theocean.fundering.domain.celebrity.service;

import com.theocean.fundering.domain.celebrity.domain.Celebrity;
import com.theocean.fundering.domain.celebrity.dto.*;
import com.theocean.fundering.domain.celebrity.repository.CelebRepository;
import com.theocean.fundering.global.errors.exception.Exception400;
import com.theocean.fundering.global.errors.exception.Exception500;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class CelebService {
    private final CelebRepository celebRepository;
    @Transactional
    public void register(final CelebRequestDTO celebRequestDTO) {
        try{
            celebRepository.save(celebRequestDTO.mapToEntity());
        }catch (final RuntimeException e){
            throw new Exception500("셀럽 등록 실패");
        }
    }

    public PageResponse<CelebFundingResponseDTO> findAllPosting(final Long celebId, final Long postId, final Pageable pageable) {
        final var page = celebRepository.findAllPosting(celebId, postId, pageable);
        return new PageResponse<>(page);
    }

    public CelebDetailsResponseDTO findByCelebId(final Long celebId) {
        final Celebrity celebrity = celebRepository.findById(celebId).orElseThrow(
                () -> new Exception400("해당 셀럽을 찾을 수 없습니다."));
        return CelebDetailsResponseDTO.from(celebrity);
    }

    public PageResponse<CelebListResponseDTO> findAllCeleb(final Long celebId, final Pageable pageable) {
        final var page = celebRepository.findAllCeleb(celebId, pageable);
        return new PageResponse<>(page);
    }
}
