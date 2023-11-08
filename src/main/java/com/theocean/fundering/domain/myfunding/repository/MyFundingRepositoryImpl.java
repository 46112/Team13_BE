package com.theocean.fundering.domain.myfunding.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.theocean.fundering.domain.myfunding.dto.MyFundingHostResponseDTO;
import com.theocean.fundering.domain.myfunding.dto.MyFundingManagerResponseDTO;
import com.theocean.fundering.domain.myfunding.dto.MyFundingSupporterResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Objects;

import static com.theocean.fundering.domain.payment.domain.QPayment.*;
import static com.theocean.fundering.domain.post.domain.QPost.post;
import static com.theocean.fundering.domain.withdrawal.domain.QWithdrawal.*;

@RequiredArgsConstructor
@Repository
public class MyFundingRepositoryImpl implements MyFundingRepository{
    private final JPAQueryFactory queryFactory;
    @Override
    public Slice<MyFundingHostResponseDTO> findAllPostingByHost(final Long userId, final Long postId, final Pageable pageable) {
        Objects.requireNonNull(postId, "postId must not be null");
        final List<MyFundingHostResponseDTO> contents = queryFactory.select(Projections.constructor(MyFundingHostResponseDTO.class,
                        post.postId,
                        post.writer.nickname,
                        post.celebrity.celebName,
                        post.celebrity.profileImage,
                        post.title,
                        post.thumbnail,
                        post.introduction,
                        post.targetPrice,
                        post.deadline,
                        post.modifiedAt,
                        post.createdAt
                ))
                .from(post)
                .where(eqPostWriterId(userId))
                .orderBy(post.postId.desc())
                .limit(pageable.getPageSize())
                .fetch();
        final boolean hasNext = contents.size() > pageable.getPageSize();
        return new SliceImpl<>(contents, pageable, hasNext);
    }

    @Override
    public Slice<MyFundingSupporterResponseDTO> findAllPostingBySupporter(final Long userId, final Long postId, final Pageable pageable) {
        Objects.requireNonNull(postId, "postId must not be null");
        final List<MyFundingSupporterResponseDTO> contents = queryFactory.select(Projections.constructor(MyFundingSupporterResponseDTO.class,
                        post.postId,
                        post.writer.nickname,
                        post.celebrity.celebName,
                        post.celebrity.profileImage,
                        post.title,
                        post.thumbnail,
                        post.introduction,
                        post.targetPrice,
                        payment.amount,
                        post.deadline,
                        post.modifiedAt,
                        post.createdAt
                ))
                .from(post, payment)
                .where(eqPostSupporterId(userId))
                .orderBy(post.postId.desc())
                .limit(pageable.getPageSize())
                .fetch();
        final boolean hasNext = contents.size() > pageable.getPageSize();
        return new SliceImpl<>(contents, pageable, hasNext);
    }

    @Override
    public Slice<MyFundingManagerResponseDTO> findAllPostingByManager(final Long userId, final Long postId, final Pageable pageable) {
        Objects.requireNonNull(postId, "postId must not be null");
        final List<MyFundingManagerResponseDTO> contents = queryFactory.select(Projections.constructor(MyFundingManagerResponseDTO.class,
                        withdrawal.withdrawalId,
                        post.postId,
                        post.thumbnail,
                        post.title,
                        post.writer.userId,
                        post.writer.profileImage,
                        post.writer.nickname,
                        withdrawal.withdrawalAmount,
                        withdrawal.usage
                ))
                .from(post)
                .where(eqPostManagerId(userId))
                .orderBy(post.postId.desc())
                .limit(pageable.getPageSize())
                .fetch();
        final boolean hasNext = contents.size() > pageable.getPageSize();
        return new SliceImpl<>(contents, pageable, hasNext);
    }

    @Override
    public void applyWithdrawal(final Long userId, final Long postId) {  // withdrawalId, postId로 변경 필요 (API 문서)
        Objects.requireNonNull(postId, "postId must not be null");
        queryFactory.update(withdrawal)
                .set(withdrawal.isApproved, true)
                .where(withdrawal.applicantId.eq(userId)
                        .and(withdrawal.postId.eq(postId)))
                .execute();
    }

    private BooleanExpression eqPostWriterId(final Long userId){
        return post.writer.userId.eq(userId);
    }

    private BooleanExpression eqPostSupporterId(final Long userId){
        return payment.member.userId.eq(userId);
    }

    private BooleanExpression eqPostManagerId(final Long userId) {
        return withdrawal.applicantId.eq(userId);
    }
}