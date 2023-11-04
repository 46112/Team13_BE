package com.theocean.fundering.domain.celebrity.repository;

import com.theocean.fundering.domain.celebrity.domain.Follow;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface FollowRepository extends JpaRepository<Follow, Follow.PK> {
    
    @Modifying
    @Query(value = "INSERT INTO follow(celebrity_id, member_id) VALUES(:celebrity_id, :member_id)", nativeQuery = true)
    void saveFollow(@Param("celebrity_id") Long celebrity_id, @Param("member_id") Long member_id);

    @Modifying
    @Query(value = "DELETE FROM follow WHERE celebrity_id = :celebrity_id AND member_id = :member_id", nativeQuery = true)
    void saveUnFollow(@Param("celebrity_id") Long celebrity_id, @Param("member_id") Long member_id);
}
