package com.theocean.fundering.domain.news.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

public class NewsRequest {

  @Getter
  @Builder
  @RequiredArgsConstructor
  public static class saveDTO {
    private final String title;
    private final String content; // 마크다운 형식의 문자열이 저장됨
  }
}
