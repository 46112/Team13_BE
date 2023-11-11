package com.theocean.fundering.domain.celebrity.service;

import com.theocean.fundering.domain.celebrity.domain.Celebrity;
import com.theocean.fundering.domain.celebrity.repository.CelebRepository;
import com.theocean.fundering.domain.celebrity.repository.FollowRepository;
import com.theocean.fundering.global.errors.exception.Exception400;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class FollowService {

    private final FollowRepository followRepository;
    private final CelebRepository celebRepository;
    private final ThreadLocal<Celebrity> threadLocal = new ThreadLocal<>();

    @Transactional
    public void followCelebs(final Long celebId, final Long memberId) {

        final Celebrity celebrity = celebRepository.findById(celebId).orElseThrow(
                () -> new Exception400("해당 셀럽을 찾을 수 없습니다.")
        );
        followRepository.saveFollow(celebrity.getCelebId(), memberId);
        threadLocal.set(celebrity.addFollowerCount());
        celebRepository.save(threadLocal.get());
        threadLocal.remove();
    }

    @Transactional
    public void unFollowCelebs(final Long celebId, final Long memberId) {

        final Celebrity celebrity = celebRepository.findById(celebId).orElseThrow(
                () -> new Exception400("해당 셀럽을 찾을 수 없습니다.")
        );
        followRepository.saveUnFollow(celebrity.getCelebId(), memberId);
        threadLocal.set(celebrity.subtractFollowerCount());
        celebRepository.save(threadLocal.get());
        threadLocal.remove();
    }
}