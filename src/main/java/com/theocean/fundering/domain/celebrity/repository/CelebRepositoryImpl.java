package com.theocean.fundering.domain.celebrity.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.theocean.fundering.domain.celebrity.dto.CelebFundingResponseDTO;
import com.theocean.fundering.domain.celebrity.dto.CelebListResponseDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;

import java.util.List;
import java.util.Objects;

import static com.theocean.fundering.domain.celebrity.domain.QCelebrity.celebrity;
import static com.theocean.fundering.domain.post.domain.QPost.post;

@RequiredArgsConstructor
@Slf4j
public class CelebRepositoryImpl implements CelebRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    @Override
    public Slice<CelebFundingResponseDTO> findAllPosting(final Long celebId, final Long postId, final Pageable pageable) {
        Objects.requireNonNull(celebId, "celebId must not be null");
        final List<CelebFundingResponseDTO> contents = queryFactory
                .select(Projections.constructor(CelebFundingResponseDTO.class,
                        post.postId,
                        post.writer.userId,
                        post.writer.nickname,
                        post.celebrity.celebId,
                        post.celebrity.celebName,
                        post.title,
                        post.introduction,
                        post.participants,
                        post.targetPrice))
                .from(post)
                .where(eqPostCelebId(celebId), ltPostId(postId))
                .orderBy(post.postId.desc())
                .limit(pageable.getPageSize())
                .fetch();
        final boolean hasNext = contents.size() > pageable.getPageSize();
        return new SliceImpl<>(contents, pageable, hasNext);
    }

    @Override
    public Slice<CelebListResponseDTO> findAllCeleb(final Long celebId, final Pageable pageable) {
        Objects.requireNonNull(celebId, "celebId must not be null");
        final List<CelebListResponseDTO> contents = queryFactory
                .select(Projections.constructor(CelebListResponseDTO.class,
                        celebrity.celebId,
                        celebrity.celebName,
                        celebrity.celebGender,
                        celebrity.celebType,
                        celebrity.celebGroup,
                        celebrity.profileImage))
                .from(celebrity)
                .where(ltCelebId(celebId))
                .orderBy(celebrity.celebId.desc())
                .limit(pageable.getPageSize())
                .fetch();
        final boolean hasNext = contents.size() > pageable.getPageSize();
        return new SliceImpl<>(contents, pageable, hasNext);
    }


    private BooleanExpression eqPostCelebId(final Long celebId){
        return post.celebrity.celebId.eq(celebId);
    }

    private BooleanExpression ltPostId(final Long cursorId){
        if (null == cursorId){
            return null;
        }
        return post.postId.lt(cursorId);
    }

    private BooleanExpression ltCelebId(final Long cursorId){
        if (null == cursorId){
            return null;
        }
        return celebrity.celebId.lt(cursorId);
    }
}
