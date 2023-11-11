package com.theocean.fundering.domain.news.service;

import com.theocean.fundering.domain.news.domain.News;
import com.theocean.fundering.domain.news.dto.NewsResponse;
import com.theocean.fundering.domain.news.repository.CustomNewsRepositoryImpl;
import com.theocean.fundering.domain.post.repository.PostRepository;
import com.theocean.fundering.global.errors.exception.ErrorCode;
import com.theocean.fundering.global.errors.exception.Exception404;
import com.theocean.fundering.global.errors.exception.Exception500;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class ReadNewsService {

    private final CustomNewsRepositoryImpl customNewsRepository;
    private final PostRepository postRepository;
    private static final int INDEX_ZERO = 0;

    // (기능) 업데이트 글 조회
    public NewsResponse.findAllDTO getNews(final long postId, final long cursor, final int pageSize) {

        validatePostExistence(postId);

        List<News> updates;

        updates = customNewsRepository.getNewsList(postId, cursor, pageSize + 1);

        // findAllDTO의 isLastPage - pageSize와 가져온 업데이트 글 수가 일치할 때를 대비해 위에서 하나 더 조회한 상태이다
        final boolean isLastPage = updates.size() <= pageSize;

        if (!isLastPage) updates = updates.subList(INDEX_ZERO, pageSize);

        // findAllDTO의 updates
        final List<NewsResponse.newsDTO> updateDTOs = convertToNewsDTOs(updates);

        // findAllDTO의 cursor
        Long lastUpdate = null;

        if (!updates.isEmpty()) {
            final News last = updates.get(updates.size() - 1);
            lastUpdate = last.getNewsId();
        }

        return new NewsResponse.findAllDTO(updateDTOs, lastUpdate, isLastPage);
    }

    private void validatePostExistence(final long postId) {
        if (!postRepository.existsById(postId)) {
            throw new Exception404(ErrorCode.ER03);
        }
    }

    private List<NewsResponse.newsDTO> convertToNewsDTOs(final List<News> updates) {

        return updates.stream().map(this::createNewsDTO).collect(Collectors.toList());
    }

    private NewsResponse.newsDTO createNewsDTO(final News update) {
        return new NewsResponse.newsDTO(update);
    }
}
