package com.theocean.fundering.domain.celebrity.repository;

import com.theocean.fundering.domain.celebrity.domain.Celebrity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CelebRepository extends JpaRepository<Celebrity, Long>, CelebRepositoryCustom {
}
