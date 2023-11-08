package com.theocean.fundering.domain.post.service;


import com.theocean.fundering.domain.celebrity.domain.Celebrity;
import com.theocean.fundering.global.dto.PageResponse;
import com.theocean.fundering.domain.celebrity.repository.CelebRepository;
import com.theocean.fundering.domain.member.domain.Member;
import com.theocean.fundering.domain.member.repository.MemberRepository;
import com.theocean.fundering.domain.post.domain.Post;
import com.theocean.fundering.domain.post.dto.PostRequest;
import com.theocean.fundering.domain.post.dto.PostResponse;
import com.theocean.fundering.domain.post.repository.PostRepository;
import com.theocean.fundering.global.errors.exception.Exception500;
import com.theocean.fundering.global.utils.AWSS3Uploader;
import jakarta.annotation.Nullable;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;


@Transactional
@RequiredArgsConstructor
@Service
public class PostService {
    private final PostRepository postRepository;
    private final AWSS3Uploader awss3Uploader;
    private final CelebRepository celebRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public void writePost(String email, PostRequest.PostWriteDTO dto, MultipartFile thumbnail){
        dto.setThumbnail(awss3Uploader.uploadToS3(thumbnail));
        Member writer =  memberRepository.findByEmail(email).orElseThrow(
                () -> new Exception500("No matched member found")
        );
        Celebrity celebrity = celebRepository.findById(dto.getCelebId()).orElseThrow(
                () -> new Exception500("No matched celebrity found")
        );
        postRepository.save(dto.toEntity(writer, celebrity));
    }

    public PostResponse.FindByPostIdDTO findByPostId(String email, Long postId){
        Post postPS = postRepository.findById(postId).orElseThrow(
                () -> new Exception500("No matched post found")
        );
        PostResponse.FindByPostIdDTO result = new PostResponse.FindByPostIdDTO(postPS);
        if (postPS.getWriter().getEmail().equals(email))
            result.setEqWriter(true);
        return result;
    }

    public PageResponse<PostResponse.FindAllDTO> findAll(@Nullable Long postId, Pageable pageable){
        var postList = postRepository.findAll(postId, pageable);
        return new PageResponse<>(postList);

    }

    public PageResponse<PostResponse.FindAllDTO> findAllByWriterId(@Nullable Long postId, String email, Pageable pageable){
        var postList = postRepository.findAllByWriterEmail(postId, email, pageable);
        return new PageResponse<>(postList);
    }

    @Transactional
    public Long editPost(Long postId, PostRequest.PostEditDTO dto, @Nullable MultipartFile thumbnail){
        if (thumbnail != null)
            dto.setThumbnail(awss3Uploader.uploadToS3(thumbnail));
        Post postPS = postRepository.findById(postId).orElseThrow(
                () -> new Exception500("No matched post found")
        );
        postPS.update(dto.getTitle(), dto.getIntroduction(), dto.getThumbnail(), dto.getTargetPrice(), dto.getDeadline(), dto.getModifiedAt());
        return postId;
    }

    public void deletePost(Long postId){
        postRepository.deleteById(postId);
    }

    public PageResponse<PostResponse.FindAllDTO> searchPost(@Nullable Long postId, String keyword, Pageable pageable){
        var postList = postRepository.findAllByKeyword(postId, keyword, pageable);
        return new PageResponse<>(postList);

    }

    public String uploadImage(MultipartFile img){
        return awss3Uploader.uploadToS3(img);
    }

    public String getIntroduction(Long postId){
        return postRepository.findById(postId).orElseThrow(
                () -> new Exception500("No mathced post found")
        ).getIntroduction();
    }

}
