package com.theocean.fundering.domain.comment.service;

import com.theocean.fundering.domain.comment.domain.Comment;
import com.theocean.fundering.domain.comment.repository.CommentRepository;
import com.theocean.fundering.global.errors.exception.Exception404;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class DeleteCommentService {

    private final CommentRepository commentRepository;
    private final CommentValidator commentValidator;

    /**
     * (기능) 댓글 삭제
     */
    @Transactional
    public void deleteComment(final Long memberId, final Long postId, final Long commentId) {
        final Comment comment =
                commentRepository
                        .findById(commentId)
                        .orElseThrow(() -> new Exception404("존재하지 않는 댓글입니다: " + commentId));

        commentValidator.validatePostExistence(postId);
        commentValidator.validateCommentOwner(memberId, comment);

        commentRepository.delete(comment);
    }
}
