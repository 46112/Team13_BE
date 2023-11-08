package com.theocean.fundering.domain.celebrity.service;

import com.theocean.fundering.domain.celebrity.domain.Celebrity;
import com.theocean.fundering.domain.celebrity.repository.CelebRepository;
import com.theocean.fundering.domain.celebrity.repository.FollowRepository;
import com.theocean.fundering.domain.member.repository.MemberRepository;
import com.theocean.fundering.global.errors.exception.Exception400;
import com.theocean.fundering.global.errors.exception.Exception500;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class FollowService {

    private final FollowRepository followRepository;
    private final CelebRepository celebRepository;

    @Transactional
    public void followCelebs(final Long celebId, final Long memberId) {

        final Celebrity celebrity = celebRepository.findById(celebId).orElseThrow(
                () -> new Exception400("해당 셀럽을 찾을 수 없습니다.")
        );
        try{
            followRepository.saveFollow(celebrity.getCelebId(), memberId);
        } catch (final RuntimeException e) {
            throw new Exception500("팔로우에 실패했습니다.");
        }
    }
    @Transactional
    public void unFollowCelebs(final Long celebId, final Long memberId) {

        final Celebrity celebrity = celebRepository.findById(celebId).orElseThrow(
                () -> new Exception400("해당 셀럽을 찾을 수 없습니다.")
        );
        try{
            followRepository.saveUnFollow(celebrity.getCelebId(), memberId);
        } catch (final RuntimeException e) {
            throw new Exception500("언팔로우에 실패했습니다.");
        }
    }
}
