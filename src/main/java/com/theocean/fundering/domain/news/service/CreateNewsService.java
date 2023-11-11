package com.theocean.fundering.domain.news.service;

import com.theocean.fundering.domain.news.domain.News;
import com.theocean.fundering.domain.news.dto.NewsRequest;
import com.theocean.fundering.domain.news.repository.NewsRepository;
import com.theocean.fundering.domain.post.domain.Post;
import com.theocean.fundering.domain.post.repository.PostRepository;
import com.theocean.fundering.global.errors.exception.ErrorCode;
import com.theocean.fundering.global.errors.exception.Exception403;
import com.theocean.fundering.global.errors.exception.Exception404;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class CreateNewsService {

    private final NewsRepository newsRepository;
    private final PostRepository postRepository;

    // (기능) 업데이트 글 작성
    @Transactional
    public void createNews(
            final Long writerId, final Long postId, final NewsRequest.SaveDTO request) {
        // DTO에서 필요한 정보를 추출합니다.
        final String title = request.getTitle();
        final String content = request.getContent();

        // 게시글 존재 여부와 작성자 확인
        final Post post =
                postRepository
                        .findById(postId)
                        .orElseThrow(() -> new Exception404(ErrorCode.ER03));

        if (!post.getWriter().getMemberId().equals(writerId))
            throw new Exception403(ErrorCode.ER15);

        // News 엔터티를 생성합니다.
        final News news =
                News.builder().postId(postId).writerId(writerId).title(title).content(content).build();

        // 저장소에 News 엔터티를 저장합니다.
        newsRepository.save(news);
    }
}
