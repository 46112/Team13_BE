package com.theocean.fundering.domain.celebrity.service;

import com.theocean.fundering.domain.celebrity.domain.Celebrity;
import com.theocean.fundering.domain.celebrity.dto.*;
import com.theocean.fundering.domain.celebrity.repository.CelebRepository;
import com.theocean.fundering.domain.celebrity.repository.FollowRepository;
import com.theocean.fundering.domain.member.repository.MemberRepository;
import com.theocean.fundering.global.dto.PageResponse;
import com.theocean.fundering.global.errors.exception.Exception400;
import com.theocean.fundering.global.errors.exception.Exception500;
import com.theocean.fundering.global.jwt.userInfo.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class CelebService {
    private static final int FOLLOW_COUNT_ZERO = 0;
    private static final long DEFAULT_MEMBER_ID = 0;

    private final CelebRepository celebRepository;

    private final FollowRepository followRepository;

    private final MemberRepository memberRepository;

    @Transactional
    public void register(final CelebRequestDTO celebRequestDTO) {
        try {
            celebRepository.save(celebRequestDTO.mapToEntity());
        } catch (final RuntimeException e) {
            throw new Exception500("셀럽 등록 실패");
        }
    }

    @Transactional
    public void approvalCelebrity(final Long celebId) {
        celebRepository.findById(celebId)
                .map(Celebrity::approvalCelebrity)
                .orElseThrow(() -> new Exception400("해당 셀럽을 찾을 수 없습니다."));
    }

    @Transactional
    public void deleteCelebrity(final Long celebId) {
        celebRepository.delete(
                celebRepository.findById(celebId).orElseThrow(
                        () -> new Exception400("해당 셀럽을 찾을 수 없습니다.")
                )
        );
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

    public PageResponse<CelebListResponseDTO> findAllCeleb(final Long celebId, final String keyword, final Pageable pageable) {
        final var page = celebRepository.findAllCeleb(celebId, keyword, pageable);
        return new PageResponse<>(page);
    }

    public PageResponse<CelebListResponseDTO> findAllCelebForApproval(final Long celebId, final Pageable pageable) {
        final var page = celebRepository.findAllCelebForApproval(celebId, pageable);
        return new PageResponse<>(page);
    }

    public List<CelebsRecommendDTO> recommendCelebs(final CustomUserDetails member) {
        final Long id = (null == member) ? DEFAULT_MEMBER_ID : member.getId();

        final List<Celebrity> celebrities = celebRepository.findAllRandom().orElseThrow(
                () -> new Exception400("해당 셀럽을 찾을 수 없습니다.")
        );
        final List<CelebsRecommendDTO> responseDTO = new ArrayList<>();
        for (final Celebrity celebrity : celebrities) {
            final int followCount = followRepository.countByCelebId(celebrity.getCelebId());
            final boolean isFollow = FOLLOW_COUNT_ZERO != followRepository.countByCelebIdAndFollowId(celebrity.getCelebId(), id);
            responseDTO.add(CelebsRecommendDTO.of(celebrity, followCount, isFollow));
        }
        return responseDTO;
    }
}
