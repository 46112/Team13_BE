package com.theocean.fundering.domain.comment.repository;

import com.theocean.fundering.domain.comment.domain.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    @Query("SELECT MAX(c.commentOrder) FROM Comment c WHERE c.postId = :postId")
    String findMaxCommentOrder(Long postId);

    @Query(
            "SELECT COUNT(c) FROM Comment c WHERE c.postId = :postId AND c.commentOrder LIKE :commentOrder")
    int countReplies(@Param("postId") Long postId, @Param("commentOrder") String commentOrder);
}
