package com.theocean.fundering.domain.myfunding.repository;

import com.theocean.fundering.domain.myfunding.dto.MyFundingHostResponseDTO;
import com.theocean.fundering.domain.myfunding.dto.MyFundingManagerResponseDTO;
import com.theocean.fundering.domain.myfunding.dto.MyFundingSupporterResponseDTO;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;


public interface MyFundingRepository {
    Slice<MyFundingHostResponseDTO> findAllPostingByHost(Long userId, Long postId, Pageable pageable);
    Slice<MyFundingSupporterResponseDTO> findAllPostingBySupporter(Long userId, Long postId, Pageable pageable);
    Slice<MyFundingManagerResponseDTO> findAllPostingByManager(Long userId, Long postId, Pageable pageable);

    void applyWithdrawal(Long userId, Long postId);
}