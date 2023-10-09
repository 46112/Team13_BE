package com.theocean.fundering.domain.celebrity.domain;

import com.theocean.fundering.celebrity.domain.constant.CelebGender;
import com.theocean.fundering.celebrity.domain.constant.CelebType;
import com.theocean.fundering.domain.follow.domain.Follow;
import com.theocean.fundering.global.utils.AuditingFields;
import com.theocean.fundering.domain.post.domain.Post;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;
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
    private Long celebId;

    @Column(nullable = false, length = 15)
    private String celebName;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CelebGender celebGender;

    @Column(nullable = false)
    private CelebType celebType;

    @Column(length = 50)
    private String celebGroup;

    @Column(nullable = false)
    private String profileImage;

    @OneToMany(mappedBy = "celebrity")
    private List<Follow> followers;

    @OneToMany(mappedBy = "celebrity")
    private List<Post> post;


    public void changeCelebName(String celebName) {
        this.celebName = celebName;
    }

    public void changeCelebGender(CelebGender celebGender) {
        this.celebGender = celebGender;
    }

    public void changeCeleType(String celebGroup) {
        this.celebType = celebType;
    }

    public void changeCeleGroup(CelebType celebType) {
        this.celebType = celebType;
    }

    public void changeProfileImage(String profileImage) {
        this.profileImage = profileImage;
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
