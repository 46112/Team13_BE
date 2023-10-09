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
@Table(name = "evidence")
@Entity
public class Evidence extends AuditingFields {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long evidenceId;

    @ManyToOne(fetch = FetchType.LAZY)
    private Withdrawal withdrawal;

    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    private Post post;

    @Builder
    public Evidence(Withdrawal withdrawal, Member member, Post post) {
        this.withdrawal = withdrawal;
        this.member = member;
        this.post = post;
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
