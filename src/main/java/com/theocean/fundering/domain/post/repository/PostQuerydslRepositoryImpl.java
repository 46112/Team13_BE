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


import static  com.theocean.fundering.domain.post.domain.QPost.post;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@RequiredArgsConstructor
public class PostQuerydslRepositoryImpl implements PostQuerydslRepository{

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Slice<PostResponse.FindAllDTO> findAll(@Nullable Long postId, Pageable pageable) {
        OrderSpecifier[] orderSpecifiers = createOrderSpecifier(pageable.getSort());

        List<PostResponse.FindAllDTO> contents =  jpaQueryFactory
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
                .orderBy(orderSpecifiers)
                .limit(pageable.getPageSize())
                .fetch();

        boolean hasNext = false;
        if(contents.size() > pageable.getPageSize()){
            hasNext = true;
        }
        return new SliceImpl<>(contents, pageable, hasNext);
    }

    @Override
    public Slice<PostResponse.FindAllDTO> findAllByWriterEmail(@Nullable Long postId, String email, Pageable pageable) {
        List<PostResponse.FindAllDTO> contents = jpaQueryFactory
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
                .where(ltPostId(postId), eqWriter(email))
                .orderBy(post.postId.desc())
                .limit(pageable.getPageSize())
                .fetch();
        boolean hasNext = false;
        if(contents.size() > pageable.getPageSize()){
            hasNext = true;
        }
        return new SliceImpl<>(contents, pageable, hasNext);
    }
    @Override
    public Slice<PostResponse.FindAllDTO> findAllByKeyword(@Nullable Long postId, String keyword, Pageable pageable){
        List<PostResponse.FindAllDTO> contents = jpaQueryFactory
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
                .where(ltPostId(postId), containKeyword(keyword))
                .orderBy(post.postId.desc())
                .limit(pageable.getPageSize())
                .fetch();
        boolean hasNext = false;
        if(contents.size() > pageable.getPageSize()){
            hasNext = true;
        }
        return new SliceImpl<>(contents, pageable, hasNext);
    }

    private OrderSpecifier[] createOrderSpecifier(Sort sort){
        List<OrderSpecifier> specifiers = new ArrayList<>();

        /* 정렬 조건 추가 시 추가 작성
        * */
        if (sort.toString().equals("recent")) {
            specifiers.add(new OrderSpecifier(Order.DESC, post.createdAt));
        } else if(sort.toString().equals("deadline")) {
            specifiers.add(new OrderSpecifier(Order.DESC, post.deadline));
        } else if(sort.toString().equals("amount")){
            specifiers.add(new OrderSpecifier(Order.DESC, post.account.balance));
        }
        return specifiers.toArray(new OrderSpecifier[specifiers.size()]);
    }

    private BooleanExpression ltPostId(@Nullable Long postId){
        if (postId == null) return null;
        return post.postId.lt(postId);
    }

    private BooleanExpression eqWriter(String email){
        return post.writer.email.eq(email);
    }
    private BooleanExpression containKeyword(String keyword){
        return post.title.contains(keyword);
    }

}
