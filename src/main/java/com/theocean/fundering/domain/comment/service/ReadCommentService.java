package com.theocean.fundering.domain.comment.service;

import com.theocean.fundering.domain.comment.domain.Comment;
import com.theocean.fundering.domain.comment.dto.CommentResponse;
import com.theocean.fundering.domain.comment.repository.CustomCommentRepositoryImpl;
import com.theocean.fundering.domain.member.domain.Member;
import com.theocean.fundering.domain.member.repository.MemberRepository;
import com.theocean.fundering.global.errors.exception.Exception404;

import java.util.List;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class ReadCommentService {

    private final CustomCommentRepositoryImpl customCommentRepository;
    private final MemberRepository memberRepository;
    private final CommentValidator commentValidator;

    /**
     * (기능) 댓글 목록 조회
     */
    public CommentResponse.FindAllDTO getComments(final long postId, final Pageable pageable) {

        commentValidator.validatePostExistence(postId);

        final Page<Comment> commentPage = customCommentRepository.getCommentsPage(postId, pageable);
        final List<Comment> comments = commentPage.getContent();

        final List<CommentResponse.CommentDTO> commentsDTOs = convertToCommentDTOs(comments);

        final boolean isLastPage = commentPage.isLast();

        final int currentPage = pageable.getPageNumber();

        return new CommentResponse.FindAllDTO(commentsDTOs, currentPage, isLastPage);
    }

    // 댓글 DTO 변환
    private List<CommentResponse.CommentDTO> convertToCommentDTOs(final List<Comment> comments) {

        return comments.stream().map(this::createCommentDTO).collect(Collectors.toList());
    }

    private CommentResponse.CommentDTO createCommentDTO(final Comment comment) {
        final Member writer =
                memberRepository
                        .findById(comment.getWriterId())
                        .orElseThrow(() -> new Exception404("존재하지 않는 회원입니다: " + comment.getWriterId()));

        final int replyCount =
                commentValidator.getReplyCount(comment.getPostId(), comment.getCommentOrder());

        return CommentResponse.CommentDTO.fromEntity(
                comment, replyCount, writer.getNickname(), writer.getProfileImage());
    }

    /**
     * (기능) 대댓글 목록 조회
     */
    public CommentResponse.FindAllDTO getSubComments(
            final long postId, final long parentCommentId, final Pageable pageable) {

        commentValidator.validatePostExistence(postId);

        commentValidator.validateCommentExistence(parentCommentId);

        final Page<Comment> commentPage =
                customCommentRepository.getSubCommentsPage(
                        postId, commentValidator.findCommentOrder(parentCommentId), pageable);
        final List<Comment> comments = commentPage.getContent();

        final List<CommentResponse.CommentDTO> commentsDTOs = convertToCommentDTOs(comments);

        final boolean isLastPage = commentPage.isLast();
        final int currentPage = pageable.getPageNumber();

        return new CommentResponse.FindAllDTO(commentsDTOs, currentPage, isLastPage);
    }
}
