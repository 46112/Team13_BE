package com.theocean.fundering.domain.evidence.repository;

import com.theocean.fundering.domain.evidence.domain.Evidence;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EvidenceRepository extends JpaRepository<Evidence, Long> {
    Optional<Evidence> findByWithdrawalId(Long withdrawalId);
}
