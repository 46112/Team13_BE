package com.theocean.fundering.domain.comment.domain;


import com.theocean.fundering.global.utils.AuditingFields;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.Objects;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name="comment")
@SQLDelete(sql = "UPDATE comment SET is_deleted = true WHERE comment_id = ?")
public class Comment extends AuditingFields {

    // PK
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long commentId;

    // 작성자의 식별자
    @Column(nullable = false)
    private Long writerId;

    // 게시물의 식별자
    @Column(nullable = false)
    private Long postId;

    // 댓글 내용
    @Column(nullable = false)
    private String content;

    // 삭제 여부
    @Column(nullable = false)
    private boolean isDeleted;

    // 대댓글 유무 - true이면 답글이 존재하므로 대댓글임을 의미함
    @Column(nullable = false)
    private boolean hasReply;

    /*
        commentOrder는 댓글이 생성될 때 부여받는 순서이다.
        Comment에서는 부모댓글의 PK를 필드값으로 갖는 대신에 부모 댓글 순서를 필드값으로 가지고 있는다.
        만약 부모댓글이 없을 경우 자기자신의 commentOrder를 parentCommentOrder필드값으로 갖는다.
    */
    @Column
    private Long parentCommentOrder;

    @Column(nullable = false)
    private Long commentOrder;

    // 대댓글 수
    @Column(nullable = false)
    private int childCommentCount;

    @Builder
    public Comment(Long writerId, Long postId, String content, Long commentOrder) {
        this.writerId = writerId;
        this.postId = postId;
        this.content = content;
        this.commentOrder = commentOrder;
        this.hasReply = false;
        this.parentCommentOrder = null;
        this.childCommentCount = 0;
        this.isDeleted = false;
    }


    public void updatehasReply(Boolean hasReply) {
        this.hasReply = hasReply;
    }

    public void updateParentCommentOrder(Long parentCommentOrder) {
        this.parentCommentOrder = parentCommentOrder;
    }

    public void increaseChildCommentCount() {
        this.childCommentCount++;
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
