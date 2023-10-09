package com.theocean.fundering.domain.comment.repository;

import com.theocean.fundering.domain.comment.domain.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;

import java.util.Optional;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    @Query("SELECT COALESCE(MAX(c.commentOrder), 0) FROM Comment c WHERE c.postId = :postId")
    Long getLastCommentOrder(@Param("postId") Long postId);

    Optional<Comment> findByPostIdAndCommentOrder(Long postId, Long commentOrder);

    @Query("SELECT c FROM Comment c WHERE c.postId = :postId ORDER BY c.parentCommentOrder, c.commentOrder")
    Page<Comment> findByPostIdOrdered(Long postId, Pageable pageable);

    boolean existsByPostIdAndCommentOrder(Long postId, Long commentOrder);


}
