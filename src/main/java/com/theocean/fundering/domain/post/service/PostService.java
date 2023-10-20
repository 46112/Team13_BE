package com.theocean.fundering.domain.post.service;


import com.theocean.fundering.domain.celebrity.domain.Celebrity;
import com.theocean.fundering.domain.celebrity.repository.CelebRepository;
import com.theocean.fundering.domain.member.domain.Member;
import com.theocean.fundering.domain.member.repository.MemberRepository;
import com.theocean.fundering.domain.post.domain.Post;
import com.theocean.fundering.domain.post.dto.PostRequest;
import com.theocean.fundering.domain.post.dto.PostResponse;
import com.theocean.fundering.domain.post.repository.PostRepository;
import com.theocean.fundering.global.utils.AWSS3Uploader;
import jakarta.annotation.Nullable;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


@Transactional
@RequiredArgsConstructor
@Service
public class PostService {
    private final PostRepository postRepository;
    private final MemberRepository memberRepository;
    private final AWSS3Uploader awss3Uploader;
    private final CelebRepository celebRepository;

    @Transactional
    public void writePost(PostRequest.PostWriteDTO postWriteDTO, MultipartFile thumbnail){
        postWriteDTO.setThumbnail(awss3Uploader.uploadToS3(thumbnail));
        Member writer = memberRepository.findByNickname(postWriteDTO.getWriter()).orElseThrow();
        Celebrity celebrity = celebRepository.findById(postWriteDTO.getCelebId()).orElseThrow();
        postRepository.save(postWriteDTO.toEntity(writer, celebrity));
    }

    public PostResponse.FindByPostIdDTO findByPostId(Long postId){
        Post postPS = postRepository.findById(postId).orElseThrow();
        return new PostResponse.FindByPostIdDTO(postPS);

    }

    public List<PostResponse.FindAllDTO> findAll(@Nullable Long postId){
        var postList = postRepository.findAll(postId);
        var checkForNext = postRepository.findAll(postList.get(postList.size() - 1).getPostId() + 1);
        if (checkForNext == null)
            postList.get(postList.size() - 1).setLast(true);
        return postList;
    }

    public List<PostResponse.FindAllDTO> findAllByWriterId(@Nullable Long postId, Long writerId){
        var postList = postRepository.findAllByWriterId(postId, writerId);
        var checkForNext = postRepository.findAllByWriterId(postList.get(postList.size() - 1).getPostId() + 1, writerId);
        if (checkForNext == null)
            postList.get(postList.size() - 1).setLast(true);
        return postList;
    }
    @Transactional
    public Long editPost(Long postId, PostRequest.PostEditDTO postEditDTO, @Nullable MultipartFile thumbnail){
        if (thumbnail != null)
            postEditDTO.setThumbnail(awss3Uploader.uploadToS3(thumbnail));
        Post postPS = postRepository.findById(postId).orElseThrow();
        postPS.update(postEditDTO);
        return postId;
    }

    public void deletePost(Long postId){
        postRepository.deleteById(postId);
    }

    public List<PostResponse.FindAllDTO> searchPost(@Nullable Long postId, String keyword){
        var postList = postRepository.findAllByKeyword(postId, keyword);
        var checkForNext = postRepository.findAllByKeyword(postList.get(postList.size() - 1).getPostId() + 1, keyword);
        if (checkForNext == null)
            postList.get(postList.size() - 1).setLast(true);
        return postList;

    }

//    public String uploadTest(MultipartFile img){
//        return awss3Uploader.uploadToS3(img);
//    }

}
