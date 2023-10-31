package com.theocean.fundering.domain.comment.domain;

import com.theocean.fundering.global.utils.AuditingFields;
import jakarta.persistence.*;

import java.time.ZoneId;
import java.util.Objects;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(
        name = "comment",
        indexes = {@Index(name = "index_comment_order", columnList = "commentOrder", unique = false)})
@SQLDelete(sql = "UPDATE comment SET is_deleted = true WHERE comment_id = ?")
public class Comment extends AuditingFields {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long commentId;

    @Column(nullable = false)
    private Long writerId;

    @Column(nullable = false)
    private Long postId;

    @Column(nullable = false)
    private String content;

    @Column(nullable = false)
    private boolean isDeleted;

    @Column(nullable = false)
    private String commentOrder;

    @Builder
    public Comment(Long writerId, Long postId, String content) {
        this.writerId = writerId;
        this.postId = postId;
        this.content = content;
        this.isDeleted = false;
    }

    public void updateCommentOrder(String commentOrder) {
        this.commentOrder = commentOrder;
    }

    public long getEpochSecond() {
        return createdAt.atZone(ZoneId.systemDefault()).toInstant().getEpochSecond();
    }

    public int getDepth() {
        return (int) commentOrder.chars().filter(ch -> ch == '.').count();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Comment comment)) return false;
        return Objects.equals(commentId, comment.commentId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(commentId);
    }
}
