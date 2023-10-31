package com.theocean.fundering.domain.comment.service;

import com.theocean.fundering.domain.comment.domain.Comment;
import com.theocean.fundering.domain.comment.dto.CommentRequest;
import com.theocean.fundering.domain.comment.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class CreateCommentService {

  private final CommentRepository commentRepository;
  private final CommentValidator commentValidator;

  /** (기능) 댓글 작성 */
  @Transactional
  public void createComment(
      final Long memberId, final Long postId, final CommentRequest.SaveDTO request) {

    commentValidator.validateMemberAndPost(memberId, postId);

    final Comment newComment = buildBaseComment(memberId, postId, request.getContent());

    createParentComment(postId, newComment);
  }

  // 기본 댓글 객체 생성
  private Comment buildBaseComment(final Long memberId, final Long postId, final String content) {
    return Comment.builder().writerId(memberId).postId(postId).content(content).build();
  }

  // 원댓글 생성
  private void createParentComment(final Long postId, final Comment newComment) {
    final String maxCommentOrder = commentRepository.findMaxCommentOrder(postId);
    final int newCommentOrder = calculateCommentOrder(maxCommentOrder);

    newComment.updateCommentOrder(String.valueOf(newCommentOrder));
    commentRepository.save(newComment);
  }

  // 생성 댓글의 commentOrder 계산
  private int calculateCommentOrder(final String maxCommentOrder) {
    if (null != maxCommentOrder) {
      final String[] parts = maxCommentOrder.split("\\.");
      return Integer.parseInt(parts[0]) + 1;
    }
    return 1;
  }

  /** (기능) 대댓글 작성 */
  @Transactional
  public void createSubComment(
      final Long memberId,
      final Long postId,
      final Long parentCommentId,
      final CommentRequest.SaveDTO request) {

    commentValidator.validateMemberAndPost(memberId, postId);

    commentValidator.validateCommentExistence(parentCommentId);

    final String content = request.getContent();

    final Comment newComment = buildBaseComment(memberId, postId, content);

    final String parentCommentOrder = commentValidator.findCommentOrder(parentCommentId);

    commentValidator.validateDepthLimit(parentCommentOrder);

    createChildComment(postId, parentCommentOrder, newComment);
  }

  // 대댓글 생성
  // @CacheEvict(key = "#postId + '_' + #parentCommentOrder", value = "replyCounts")
  private void createChildComment(
          final Long postId, final String parentCommentOrder, final Comment newComment) {

    final int replyCount = commentValidator.validateReplyLimit(postId, parentCommentOrder);

    final String newCommentOrder = parentCommentOrder + "." + (replyCount + 1);

    newComment.updateCommentOrder(newCommentOrder);
    commentRepository.save(newComment);
  }
}
