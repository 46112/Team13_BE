package com.theocean.fundering.domain.post.repository;

import com.theocean.fundering.domain.post.dto.PostResponse;
import jakarta.annotation.Nullable;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

import java.util.List;

public interface PostQuerydslRepository {
    Slice<PostResponse.FindAllDTO> findAll(@Nullable Long postId, Pageable pageable);

    Slice<PostResponse.FindAllDTO> findAllByWriterEmail(@Nullable Long postId, String email, Pageable pageable);
    Slice<PostResponse.FindAllDTO> findAllByKeyword(@Nullable Long postId, String keyword, Pageable pageable);
}
