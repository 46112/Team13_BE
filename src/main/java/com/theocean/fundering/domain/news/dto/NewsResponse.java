package com.theocean.fundering.domain.news.dto;

import com.theocean.fundering.domain.news.domain.News;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import lombok.Getter;

public class NewsResponse {
  @Getter
  public static class findAllDTO {
    private final List<NewsResponse.newsDTO> updates;
    private final Long cursor;
    private final boolean isLastPage;

    public findAllDTO(List<NewsResponse.newsDTO> updates, Long cursor, boolean isLastPage) {
      this.updates = updates;
      this.cursor = cursor;
      this.isLastPage = isLastPage;
    }

    public boolean getIsLastPage() {
      return isLastPage;
    }
  }

  // findAllDTO의 내부에 들어갈 업데이트 글 정보 DTO
  @Getter
  public static class newsDTO {
    private final Long updateId;
    private final String title;
    private final String content;
    private final long createdAt;

    public newsDTO(News news) {
      this.updateId = news.getNewsId();
      this.title = news.getTitle();
      this.content = news.getContent();
      this.createdAt = toEpochSecond(news.getCreatedAt());
    }

    private long toEpochSecond(LocalDateTime localDateTime) {
      return localDateTime.atZone(ZoneId.systemDefault()).toInstant().getEpochSecond();
    }
  }
}
