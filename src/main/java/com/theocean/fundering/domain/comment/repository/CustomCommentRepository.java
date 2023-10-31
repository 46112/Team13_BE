package com.theocean.fundering.domain.comment.repository;

import com.theocean.fundering.domain.comment.domain.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CustomCommentRepository {

    Page<Comment> getCommentsPage(Long postId, Pageable pageable);

    Page<Comment> getSubCommentsPage(Long postId, String parentCommentOrder, Pageable pageable);
}
