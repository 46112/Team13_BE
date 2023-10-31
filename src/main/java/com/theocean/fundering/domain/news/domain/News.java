package com.theocean.fundering.domain.news.domain;

import com.theocean.fundering.global.utils.AuditingFields;
import jakarta.persistence.*;

import java.util.Objects;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "news")
public class News extends AuditingFields {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long newsId;

    @Column(nullable = false)
    private Long postId;

    @Column(nullable = false)
    private Long writerId;

    @Column(nullable = false, length = 100)
    private String title;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    @Column
    private String imageUrls; // TODO: 추후 리팩토링 예정

    @Builder
    public News(Long postId, Long writerId, String title, String content) {
        this.postId = postId;
        this.writerId = writerId;
        this.title = title;
        this.content = content;
    }

    public void updateImageUrls(String imageUrls) {
        this.imageUrls = imageUrls;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof News news)) return false;
        return Objects.equals(newsId, news.newsId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(newsId);
    }
}
