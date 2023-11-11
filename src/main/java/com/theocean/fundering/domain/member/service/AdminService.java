package com.theocean.fundering.domain.member.service;

import com.theocean.fundering.domain.member.domain.Member;
import com.theocean.fundering.domain.member.dto.AdminResponse;
import com.theocean.fundering.domain.member.repository.AdminRepository;
import com.theocean.fundering.domain.member.repository.MemberRepository;
import com.theocean.fundering.domain.post.domain.Post;
import com.theocean.fundering.domain.post.repository.PostRepository;
import com.theocean.fundering.global.errors.exception.ErrorCode;
import com.theocean.fundering.global.errors.exception.Exception404;
import com.theocean.fundering.global.errors.exception.Exception500;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class AdminService {

    private final AdminRepository adminRepository;
    private final MemberRepository memberRepository;
    private final PostRepository postRepository;
    private static final int ADMIN_COUNT = 3;

    public List<AdminResponse.FindAllDTO> getAdmins(final Long postId){

        final Optional<Post> post = postRepository.findById(postId);
        if (post.isEmpty()) throw new Exception404(ErrorCode.ER03);

        final List<Long> adminIdList = adminRepository.findByPostId(postId);
        if (adminIdList.size() > ADMIN_COUNT) throw new Exception500(ErrorCode.ER05);
        final List<AdminResponse.FindAllDTO> adminDetails = new ArrayList<>(ADMIN_COUNT);

        for(final Long adminId : adminIdList) {
            final Member member = memberRepository.findById(adminId)
                    .orElseThrow(() -> new Exception404(ErrorCode.ER01));

            final AdminResponse.FindAllDTO dto = AdminResponse.FindAllDTO.fromEntity(
                    adminId,
                    member.getProfileImage(),
                    member.getNickname()
            );
            adminDetails.add(dto);
        }
        return adminDetails;
    }
}
