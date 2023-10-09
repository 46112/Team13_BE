package com.theocean.fundering.domain.like.domain;


import com.theocean.fundering.domain.celebrity.domain.Celebrity;
import com.theocean.fundering.domain.member.domain.Member;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.Objects;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name="heart")
@Entity
public class Heart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long heartId;

    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;


    @ManyToOne(fetch = FetchType.LAZY)
    private Celebrity celeb;


    @Builder
    public Heart(Member member, Celebrity celeb) {
        this.member = member;
        this.celeb = celeb;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Heart heart)) return false;
        return Objects.equals(heartId, heart.heartId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(heartId);
    }
}
