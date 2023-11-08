package com.theocean.fundering.domain.myfunding.service;

import com.theocean.fundering.domain.celebrity.domain.Celebrity;
import com.theocean.fundering.domain.celebrity.repository.CelebRepository;
import com.theocean.fundering.domain.celebrity.repository.FollowRepository;
import com.theocean.fundering.domain.member.domain.Member;
import com.theocean.fundering.domain.member.repository.MemberRepository;
import com.theocean.fundering.domain.myfunding.dto.MyFundingFollowingCelebsDTO;
import com.theocean.fundering.domain.myfunding.dto.MyFundingHostResponseDTO;
import com.theocean.fundering.domain.myfunding.dto.MyFundingManagerResponseDTO;
import com.theocean.fundering.domain.myfunding.dto.MyFundingSupporterResponseDTO;
import com.theocean.fundering.domain.myfunding.repository.MyFundingRepository;
import com.theocean.fundering.global.dto.PageResponse;
import com.theocean.fundering.global.errors.exception.Exception400;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class MyFundingService {
    private final MyFundingRepository myFundingRepository;
    private final MemberRepository memberRepository;
    private final FollowRepository followRepository;
    private final CelebRepository celebRepository;
    public PageResponse<MyFundingHostResponseDTO> findAllPostingByHost(final Long userId, final Long postId, final Pageable pageable) {
        final var page = myFundingRepository.findAllPostingByHost(userId, postId, pageable);
        return new PageResponse<>(page);
    }

    public PageResponse<MyFundingSupporterResponseDTO> findAllPostingBySupporter(final Long userId, final Long postId, final Pageable pageable) {
        final var page = myFundingRepository.findAllPostingBySupporter(userId, postId, pageable);
        return new PageResponse<>(page);
    }

    public String getNickname(final Long id) {
        final Member member = memberRepository.findById(id).orElseThrow(
                () -> new Exception400("회원을 찾을 수 없습니다.")
        );
        return member.getNickname();
    }

    public List<MyFundingFollowingCelebsDTO> findFollowingCelebs(final Long userId) {
        final List<MyFundingFollowingCelebsDTO> responseDTO = new ArrayList<>();
        final List<Long> allFollowingCelebId = followRepository.findAllFollowingCelebById(userId);
        for(final Long celebId: allFollowingCelebId){
            final Celebrity celebrity = celebRepository.findById(celebId).orElseThrow(
                    () -> new Exception400("셀럽을 찾을 수 없습니다.")
            );
            final int followerCount = followRepository.countByCelebId(celebId);
            responseDTO.add(MyFundingFollowingCelebsDTO.of(celebrity, followerCount));
        }
        return responseDTO;
    }

    public PageResponse<MyFundingManagerResponseDTO> findAllPostingByManager(Long userId, Long postId, Pageable pageable) {
        var page = myFundingRepository.findAllPostingByManager(userId, postId, pageable);
        return new PageResponse<>(page);
    }

    public void applyWithdrawal(Long userId, Long postId) {
        myFundingRepository.applyWithdrawal(userId, postId);
    }
}
