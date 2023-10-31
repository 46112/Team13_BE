package com.theocean.fundering.domain.news.repository;

import static com.theocean.fundering.domain.news.domain.QNews.news;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.theocean.fundering.domain.news.domain.News;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class CustomNewsRepositoryImpl implements CustomNewsRepository {

  private static final long DEFAULT_CURSOR = 0;
  private final JPAQueryFactory queryFactory;

  public List<News> getNewsList(long postId, long cursor, int pageSize) {
    BooleanExpression whereCondition = news.postId.eq(postId);
    if (cursor > DEFAULT_CURSOR) {
      whereCondition = whereCondition.and(news.newsId.lt(cursor));
    }

    return queryFactory
        .selectFrom(news)
        .where(whereCondition)
        .orderBy(news.newsId.desc())
        .limit(pageSize)
        .fetch();
  }
}
