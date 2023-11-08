package com.theocean.fundering.domain.celebrity.domain;

import com.theocean.fundering.domain.celebrity.domain.constant.ApprovalStatus;
import com.theocean.fundering.domain.celebrity.domain.constant.CelebGender;
import com.theocean.fundering.domain.celebrity.domain.constant.CelebType;
import com.theocean.fundering.global.utils.AuditingFields;
import jakarta.persistence.*;
import lombok.*;

import java.util.Objects;

@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "celebrity",
        indexes = @Index(columnList = "celebName")
)
@Entity
public class Celebrity extends AuditingFields {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long celebId;

    @Column(nullable = false, length = 15)
    private String celebName;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CelebGender celebGender;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CelebType celebType;

    @Column(length = 50)
    private String celebGroup;

    @Column(nullable = false)
    private String profileImage;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ApprovalStatus status = ApprovalStatus.PENDING;

    public Celebrity approvalCelebrity(){
        status = ApprovalStatus.APPROVED;
        return this;
    }

    @Builder
    public Celebrity(String celebName, CelebGender celebGender, CelebType celebType,
                     String celebGroup, String profileImage) {
        this.celebName = celebName;
        this.celebGender = celebGender;
        this.celebType = celebType;
        this.celebGroup = celebGroup;
        this.profileImage = profileImage;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Celebrity celebrity)) return false;
        return Objects.equals(celebId, celebrity.celebId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(celebId);
    }
}
