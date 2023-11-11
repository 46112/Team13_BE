package com.theocean.fundering.domain.member.repository;

import com.theocean.fundering.domain.member.domain.Admin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AdminRepository extends JpaRepository<Admin, Admin.PK> {
    @Query("SELECT a.postId FROM Admin a WHERE a.memberId = :memberId")
    List<Long> findByMemberId(@Param("memberId")Long memberId);

    @Query("SELECT a.memberId FROM Admin a WHERE a.postId = :postId")
    List<Long> findByPostId(@Param("postId")Long postId);

}
