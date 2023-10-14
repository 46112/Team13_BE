package com.theocean.fundering.domain.news.service;

import com.theocean.fundering.domain.news.domain.News;
import com.theocean.fundering.domain.news.dto.NewsRequest;
import com.theocean.fundering.domain.news.repository.NewsRepository;
import com.theocean.fundering.domain.post.domain.Post;
import com.theocean.fundering.domain.post.repository.PostRepository;
import com.theocean.fundering.global.errors.exception.Exception403;
import com.theocean.fundering.global.errors.exception.Exception404;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class NewsService {

    private final NewsRepository newsRepository;
    private final PostRepository postRepository;

    @Transactional
    public void createNews(Long writerId, Long postId, NewsRequest.saveDTO request) {
        // DTO에서 필요한 정보를 추출합니다.
        String title = request.getTitle();
        String content = request.getContent();
        boolean viewRestriction = request.isViewRestriction();


        // 게시글 존재 여부와 작성자 확인
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new Exception404("게시글을 찾을 수 없습니다: " + postId));

        if (!post.getWriter().getUserId().equals(writerId)) {
            throw new Exception403("주최자만 업데이트 글을 작성할 수 있습니다.");
        }

        // News 엔터티를 생성합니다.
        News news = News.builder().postId(postId).writerId(writerId).title(title).content(content).viewRestriction(viewRestriction).build();

        // 저장소에 News 엔터티를 저장합니다.
        newsRepository.save(news);
    }
}