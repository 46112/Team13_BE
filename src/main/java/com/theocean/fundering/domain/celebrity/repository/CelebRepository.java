package com.theocean.fundering.domain.celebrity.repository;

import com.theocean.fundering.domain.celebrity.domain.Celebrity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface CelebRepository extends JpaRepository<Celebrity, Long>, CelebRepositoryCustom {
    @Query(value = "SELECT * FROM celebrity order by RAND() limit 3", nativeQuery = true)
    Optional<List<Celebrity>> findAllRandom();
}
