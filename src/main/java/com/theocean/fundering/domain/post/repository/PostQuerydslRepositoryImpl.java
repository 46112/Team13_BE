package com.theocean.fundering.domain.post.repository;

import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.theocean.fundering.domain.post.dto.PostResponse;
import jakarta.annotation.Nullable;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.data.domain.Sort;

import java.util.ArrayList;
import java.util.List;

import static com.theocean.fundering.domain.post.domain.QPost.post;

@RequiredArgsConstructor
public class PostQuerydslRepositoryImpl implements PostQuerydslRepository {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Slice<PostResponse.FindAllDTO> findAllInfiniteScroll(final Pageable pageable) {
        final OrderSpecifier[] orderSpecifiers = createOrderSpecifier(pageable.getSort());

        final List<PostResponse.FindAllDTO> contents = jpaQueryFactory
                .select(Projections.bean(PostResponse.FindAllDTO.class,
                        post.postId.as("postId"),
                        post.writer.memberId.as("writerId"),
                        post.writer.nickname.as("writer"),
                        post.celebrity.celebId.as("celebId"),
                        post.celebrity.celebName.as("celebrity"),
                        post.celebrity.profileImage.as("celebImg"),
                        post.title.as("title"),
                        post.thumbnail.as("thumbnail"),
                        post.targetPrice.as("targetPrice"),
                        post.account.balance.as("currentAmount"),
                        post.deadline.as("deadline"),
                        post.createdAt.as("createdAt"),
                        post.modifiedAt.as("modifiedAt"),
                        post.heartCount.as("heartCount")))
                .from(post)
                .offset(pageable.getOffset())
                .orderBy(orderSpecifiers)
                .limit(pageable.getPageSize() + 1)
                .fetch();

        return new SliceImpl<>(contents, pageable, hasNext(contents, pageable));
    }

    @Override
    public Slice<PostResponse.FindAllDTO> findAllByWriterName(final String nickname, final Pageable pageable) {
        final List<PostResponse.FindAllDTO> contents = jpaQueryFactory
                .select(Projections.bean(PostResponse.FindAllDTO.class,
                        post.postId.as("postId"),
                        post.writer.memberId.as("writerId"),
                        post.writer.nickname.as("writer"),
                        post.celebrity.celebId.as("celebId"),
                        post.celebrity.celebName.as("celebrity"),
                        post.celebrity.profileImage.as("celebImg"),
                        post.title.as("title"),
                        post.thumbnail.as("thumbnail"),
                        post.targetPrice.as("targetPrice"),
                        post.account.balance.as("currentAmount"),
                        post.deadline.as("deadline"),
                        post.createdAt.as("createdAt"),
                        post.modifiedAt.as("modifiedAt"),
                        post.heartCount.as("heartCount")))
                .from(post)
                .where(eqWriter(nickname))
                .offset(pageable.getOffset())
                .orderBy(post.postId.desc())
                .limit(pageable.getPageSize() + 1)
                .fetch();

        return new SliceImpl<>(contents, pageable, hasNext(contents, pageable));
    }

    @Override
    public Slice<PostResponse.FindAllDTO> findAllByKeyword(final String keyword, final Pageable pageable) {
        final List<PostResponse.FindAllDTO> contents = jpaQueryFactory
                .select(Projections.bean(PostResponse.FindAllDTO.class,
                        post.postId.as("postId"),
                        post.writer.memberId.as("writerId"),
                        post.writer.nickname.as("writer"),
                        post.celebrity.celebId.as("celebId"),
                        post.celebrity.celebName.as("celebrity"),
                        post.celebrity.profileImage.as("celebImg"),
                        post.title.as("title"),
                        post.thumbnail.as("thumbnail"),
                        post.targetPrice.as("targetPrice"),
                        post.account.balance.as("currentAmount"),
                        post.deadline.as("deadline"),
                        post.createdAt.as("createdAt"),
                        post.modifiedAt.as("modifiedAt"),
                        post.heartCount.as("heartCount")))
                .from(post)
                .where(containKeyword(keyword))
                .offset(pageable.getOffset())
                .orderBy(post.postId.desc())
                .limit(pageable.getPageSize() + 1)
                .fetch();

        return new SliceImpl<>(contents, pageable, hasNext(contents, pageable));
    }

    private OrderSpecifier[] createOrderSpecifier(final Sort sort) {
        final List<OrderSpecifier> specifiers = new ArrayList<>();

        /* 정렬 조건 추가 시 추가 작성
         * */
        if ("recent".equals(sort.toString())) {
            specifiers.add(new OrderSpecifier(Order.DESC, post.createdAt));
        } else if ("deadline".equals(sort.toString())) {
            specifiers.add(new OrderSpecifier(Order.DESC, post.deadline));
        } else if ("amount".equals(sort.toString())) {
            specifiers.add(new OrderSpecifier(Order.DESC, post.account.balance));
        }
        return specifiers.toArray(new OrderSpecifier[specifiers.size()]);
    }

    private BooleanExpression ltPostId(@Nullable final Long postId) {
        if (null == postId) return null;
        return post.postId.lt(postId);
    }

    private BooleanExpression eqWriter(final String nickname) {
        return post.writer.nickname.contains(nickname);
    }

    private BooleanExpression containKeyword(final String keyword) {
        return post.title.contains(keyword);
    }

    private boolean hasNext(final List<?> contents, final Pageable pageable) {
        if (contents.size() > pageable.getPageSize()) {
            contents.remove(contents.size() - 1);
            return true;
        }
        return false;
    }

}
