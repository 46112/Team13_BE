package com.theocean.fundering.domain.evidence.domain;


import com.theocean.fundering.global.utils.AuditingFields;
import com.theocean.fundering.domain.member.domain.Member;
import com.theocean.fundering.domain.post.domain.Post;
import com.theocean.fundering.domain.withdrawal.domain.Withdrawal;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Objects;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "evidence", indexes = {@Index(name = "index_withdrawal", columnList = "withdrawalId", unique = true)})
@Entity
public class Evidence extends AuditingFields {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long evidenceId;

    @Column(nullable = false)
    private Long withdrawalId;

    @Column(nullable = false)
    private Long applicantId;

    @Column(nullable = false)
    private Long postId;

    @Column(nullable = false)
    private String url;

    @Builder
    public Evidence(Long withdrawalId, Long applicantId, Long postId, String url) {
        this.withdrawalId = withdrawalId;
        this.applicantId = applicantId;
        this.postId = postId;
        this.url = url;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Evidence evidence)) return false;
        return Objects.equals(evidenceId, evidence.evidenceId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(evidenceId);
    }
}
