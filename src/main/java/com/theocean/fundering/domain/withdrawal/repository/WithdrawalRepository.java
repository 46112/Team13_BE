package com.theocean.fundering.domain.withdrawal.repository;

import com.theocean.fundering.domain.withdrawal.domain.Withdrawal;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface WithdrawalRepository extends JpaRepository<Withdrawal, Long> {
    @Query("SELECT w FROM Withdrawal w WHERE w.postId = :postId AND w.isApproved = true ORDER BY w.modifiedAt DESC")
    Page<Withdrawal> getWithdrawalPage(@Param("postId") Long postId, Pageable pageable);

}
