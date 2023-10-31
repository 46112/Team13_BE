package com.theocean.fundering.domain.comment.service;

import com.theocean.fundering.domain.comment.domain.Comment;
import com.theocean.fundering.domain.comment.repository.CommentRepository;
import com.theocean.fundering.domain.member.repository.MemberRepository;
import com.theocean.fundering.domain.post.repository.PostRepository;
import com.theocean.fundering.global.errors.exception.Exception400;
import com.theocean.fundering.global.errors.exception.Exception403;
import com.theocean.fundering.global.errors.exception.Exception404;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class CommentValidator {

    private final MemberRepository memberRepository;
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;
    private static final int REPLY_LIMIT = 30;

    public void validateMemberAndPost(final Long memberId, final Long postId) {
        if (!memberRepository.existsById(memberId))
            throw new Exception404("존재하지 않는 회원입니다: " + memberId);

        validatePostExistence(postId);
    }

    public void validatePostExistence(final long postId) {
        if (!postRepository.existsById(postId)) throw new Exception404("해당 게시글을 찾을 수 없습니다: " + postId);
    }

    public void validateCommentExistence(final long commentId) {
        final Comment parentComment =
                commentRepository
                        .findById(commentId)
                        .orElseThrow(() -> new Exception404("존재하지 않는 댓글입니다: " + commentId));

        if (parentComment.isDeleted()) throw new Exception400("삭제된 댓글입니다.");
    }

    public void validateCommentOwner(final Long memberId, final Comment comment) {

        if (!memberId.equals(comment.getWriterId())) throw new Exception403("댓글 삭제 권한이 없습니다.");
    }

    public void validateDepthLimit(final String commentOrder) {
        if (commentOrder.contains(".")) throw new Exception400("대댓글에는 댓글을 달 수 없습니다.");
    }

    @CacheEvict(key = "#postId + '_' + #parentCommentOrder", value = "replyCounts")
    public int validateReplyLimit(final Long postId, final String parentCommentOrder) {
        final int replyCount = commentRepository.countReplies(postId, parentCommentOrder + "%.%");
        if (REPLY_LIMIT <= replyCount) throw new Exception400("더 이상 대댓글을 달 수 없습니다.");
        return replyCount;
    }

    @Cacheable(key = "#postId + '_' + #commentOrder", value = "replyCounts")
    public int getReplyCount(final Long postId, final String commentOrder) {
        if (commentOrder.contains(".")) return 0; // 대댓글의 경우, 0을 반환

        return commentRepository.countReplies(postId, commentOrder + "%.%");
    }

    // 원댓글의 commentOrder 반환
    public String findCommentOrder(final Long commentId) {
        final Comment comment =
                commentRepository
                        .findById(commentId)
                        .orElseThrow(() -> new Exception404("존재하지 않는 댓글입니다: " + commentId));

        return comment.getCommentOrder();
    }
}
