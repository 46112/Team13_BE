package com.theocean.fundering.domain.celebrity.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.io.Serializable;



@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(
        name = "follow",
        indexes = {
                @Index(name = "idx_celebrity_id", columnList = "celebrity_id"),
                @Index(name = "idx_member_id", columnList = "member_id")
        },
        uniqueConstraints = @UniqueConstraint(columnNames = {"celebrity_id", "member_id"}))
@IdClass(Follow.PK.class)
@Entity
public class Follow{
    @Id
    @Column(name="celebrity_id", insertable = false, updatable = false)
    private Long celebrityId;

    @Id
    @Column(name="member_id", insertable = false, updatable = false)
    private Long memberId;

    //Follow 관계의 유일성을 위한 복합키 설정
    public static class PK implements Serializable {
        Long celebrityId;
        Long memberId;
    }
}
