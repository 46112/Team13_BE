package com.theocean.fundering.domain.admin.domain;

import com.theocean.fundering.domain.account.domain.Account;
import com.theocean.fundering.global.utils.AuditingFields;
import com.theocean.fundering.domain.post.domain.Post;
import com.theocean.fundering.domain.member.domain.Member;
import lombok.*;
import jakarta.persistence.*;

@Getter
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "admin")
@Entity
public class Admin extends AuditingFields {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long adminId;

    // 유저
    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

    // 게시물
    @ManyToOne(fetch = FetchType.LAZY)
    private Post post;

    // 계좌
    @ManyToOne(fetch = FetchType.LAZY)
    private Account account;

    // 생성자
    @Builder
    public Admin(Member member, Post post, Account account) {
        this.member = member;
        this.post = post;
        this.account = account;
    }

}
