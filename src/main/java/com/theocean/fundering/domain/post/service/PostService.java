package com.theocean.fundering.domain.post.service;


import com.theocean.fundering.domain.account.domain.Account;
import com.theocean.fundering.domain.account.repository.AccountRepository;
import com.theocean.fundering.domain.celebrity.domain.Celebrity;
import com.theocean.fundering.domain.celebrity.repository.CelebRepository;
import com.theocean.fundering.domain.celebrity.repository.FollowRepository;
import com.theocean.fundering.domain.member.domain.Member;
import com.theocean.fundering.domain.member.repository.MemberRepository;
import com.theocean.fundering.domain.post.domain.Post;
import com.theocean.fundering.domain.post.domain.constant.PostStatus;
import com.theocean.fundering.domain.post.dto.PostRequest;
import com.theocean.fundering.domain.post.dto.PostResponse;
import com.theocean.fundering.domain.post.repository.HeartRepository;
import com.theocean.fundering.domain.post.repository.PostRepository;
import com.theocean.fundering.global.dto.PageResponse;
import com.theocean.fundering.global.errors.exception.ErrorCode;
import com.theocean.fundering.global.errors.exception.Exception403;
import com.theocean.fundering.global.errors.exception.Exception500;
import com.theocean.fundering.global.utils.AWSS3Uploader;
import jakarta.annotation.Nullable;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import java.time.LocalDateTime;


@RequiredArgsConstructor
@Service
public class PostService {
    private static final int FOLLOW_COUNT_ZERO = 0;
    private static final int HEART_COUNT_ZERO = 0;
    private final PostRepository postRepository;
    private final AWSS3Uploader awss3Uploader;
    private final CelebRepository celebRepository;
    private final MemberRepository memberRepository;
    private final AccountRepository accountRepository;
    private final FollowRepository followRepository;
    private final HeartRepository heartRepository;

    @Transactional
    public void writePost(final String email, final PostRequest.PostWriteDTO dto, final MultipartFile thumbnail) {
        final String thumbnailURL = awss3Uploader.uploadToS3(thumbnail);
        final Member writer = memberRepository.findByEmail(email).orElseThrow(
                () -> new Exception500(ErrorCode.ER01)
        );
        final Celebrity celebrity = celebRepository.findById(dto.getCelebId()).orElseThrow(
                () -> new Exception500(ErrorCode.ER02)
        );
        final LocalDateTime deadline = LocalDateTime.parse(dto.getDeadline());
        final Post newPost = postRepository.save(dto.toEntity(writer, celebrity, thumbnailURL, deadline, PostStatus.ONGOING));

        final Account account = Account.builder()
                .managerId(writer.getMemberId())
                .postId(newPost.getPostId())
                .build();
        accountRepository.save(account);
        newPost.registerAccount(account);
    }

    @Transactional
    public PostResponse.FindByPostIdDTO findByPostId(final String email, final Long postId) {
        final Post postPS = postRepository.findById(postId).orElseThrow(
                () -> new Exception500(ErrorCode.ER03)
        );
        final PostResponse.FindByPostIdDTO result = new PostResponse.FindByPostIdDTO(postPS);

        if (null != email) {
            final Member member = memberRepository.findByEmail(email).orElseThrow();
            final boolean isFollowed = FOLLOW_COUNT_ZERO != followRepository.countByCelebIdAndFollowId(postPS.getCelebrity().getCelebId(), member.getMemberId());
            final boolean isHeart = HEART_COUNT_ZERO != heartRepository.countByPostIdAndHeartId(postPS.getPostId(), member.getMemberId());
            if (postPS.getWriter().getEmail().equals(email))
                result.setEqWriter(true);
            result.setFollowed(isFollowed);
            result.setHeart(isHeart);
        }

        return result;
    }

    @Transactional
    public PageResponse<PostResponse.FindAllDTO> findAll(final String email, final Pageable pageable) {
        final var postList = postRepository.findAllInfiniteScroll(pageable);
        if (null != email) {
            final Member member = memberRepository.findByEmail(email).orElseThrow();
            postList.stream().filter(p -> HEART_COUNT_ZERO != heartRepository.countByPostIdAndHeartId(p.getPostId(), member.getMemberId())).forEach(p -> p.setHeart(true));
        }
        return new PageResponse<>(postList);

    }

    @Transactional
    public PageResponse<PostResponse.FindAllDTO> findAllByWriterName(final String email, final String nickname, final Pageable pageable) {
        final var postList = postRepository.findAllByWriterName(nickname, pageable);
        if (null != email) {
            final Member member = memberRepository.findByEmail(email).orElseThrow();
            postList.stream().filter(p -> HEART_COUNT_ZERO != heartRepository.countByPostIdAndHeartId(p.getPostId(), member.getMemberId())).forEach(p -> p.setHeart(true));
        }
        return new PageResponse<>(postList);
    }

    @Transactional
    public Long editPost(final Long postId, final String email, final PostRequest.PostEditDTO dto, @Nullable final MultipartFile thumbnail) {
        String newThumbnail = null;
        final Post postPS = postRepository.findById(postId).orElseThrow(
                () -> new Exception500(ErrorCode.ER03)
        );
        if (null != thumbnail)
            newThumbnail = awss3Uploader.uploadToS3(thumbnail);
        else
            newThumbnail = postPS.getThumbnail();
        if (!postPS.getWriter().getEmail().equals(email))
            throw new Exception403(ErrorCode.ER15);
        postPS.update(dto.getTitle(), dto.getIntroduction(), newThumbnail, dto.getTargetPrice(), LocalDateTime.parse(dto.getDeadline()), dto.getModifiedAt());
        return postId;
    }

    @Transactional
    public void deletePost(final Long postId) {
        postRepository.deleteById(postId);
    }

    @Transactional
    public PageResponse<PostResponse.FindAllDTO> findAllByKeyword(final String email, final String keyword, final Pageable pageable) {
        final var postList = postRepository.findAllByKeyword(keyword, pageable);
        if (null != email) {
            final Member member = memberRepository.findByEmail(email).orElseThrow();
            postList.stream().filter(p -> HEART_COUNT_ZERO != heartRepository.countByPostIdAndHeartId(p.getPostId(), member.getMemberId())).forEach(p -> p.setHeart(true));
        }
        return new PageResponse<>(postList);

    }

    public String getIntroduction(final Long postId) {
        return postRepository.findById(postId).orElseThrow(
                () -> new Exception500(ErrorCode.ER03)
        ).getIntroduction();
    }


}
