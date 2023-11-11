package com.theocean.fundering.domain.member.repository;

import com.theocean.fundering.domain.member.dto.MyFundingResponse;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;


public interface MyFundingRepository {
    Slice<MyFundingResponse.HostDTO> findAllPostingByHost(Long memberId, Pageable pageable);
    Slice<MyFundingResponse.SupporterDTO> findAllPostingBySupporter(Long memberId, Pageable pageable);

    Slice<MyFundingResponse.HeartPostingDTO> findAllPostingByHeart(Long memberId, Pageable pageable);

}