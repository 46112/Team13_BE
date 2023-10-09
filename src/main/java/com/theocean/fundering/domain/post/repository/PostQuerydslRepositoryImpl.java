package com.theocean.fundering.domain.post.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.theocean.fundering.domain.post.dto.PostResponse;
import lombok.RequiredArgsConstructor;
import static  com.theocean.fundering.domain.post.domain.QPost.post;

import java.util.List;

@RequiredArgsConstructor
public class PostQuerydslRepositoryImpl implements PostQuerydslRepository{

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<PostResponse.FindAllDTO> findAll(Long postId, int pageSize) {
        return jpaQueryFactory
                .select(Projections.bean(PostResponse.FindAllDTO.class,
                        post.title,
                        post.thumbnail,
                        post.writer,
                        post.celebrity,
                        post.targetPrice,
                        post.account,
                        post.deadline,
                        post.createdAt))
                .from(post)
                .where(ltPostId(postId))
                .orderBy(post.postId.desc())
                .limit(pageSize)
                .fetch();
    }

    @Override
    public List<PostResponse.FindAllDTO> findAllByWriterId(Long postId, Long writerId, int pageSize) {
        return jpaQueryFactory
                .select(Projections.bean(PostResponse.FindAllDTO.class,
                        post.title,
                        post.thumbnail,
                        post.writer,
                        post.celebrity,
                        post.targetPrice,
                        post.account,
                        post.deadline,
                        post.createdAt))
                .from(post)
                .where(ltPostId(postId), eqWriter(writerId))
                .orderBy(post.postId.desc())
                .limit(pageSize)
                .fetch();
    }


    private BooleanExpression ltPostId(Long postId){
        if (postId == null){
            return null;
        }
        return post.postId.lt(postId);
    }

    private BooleanExpression eqWriter(Long writerId){
        return post.writer.userId.eq(writerId);
    }
}
