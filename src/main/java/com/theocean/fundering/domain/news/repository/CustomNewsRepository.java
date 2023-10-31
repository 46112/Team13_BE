package com.theocean.fundering.domain.news.repository;

import com.theocean.fundering.domain.news.domain.News;
import java.util.List;

public interface CustomNewsRepository {
  List<News> getNewsList(long postId, long cursor, int pageSize);
}
