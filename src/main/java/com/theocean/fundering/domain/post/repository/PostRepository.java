package com.theocean.fundering.domain.post.repository;


import com.theocean.fundering.domain.post.domain.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;


public interface PostRepository extends JpaRepository<Post, Long>, PostQuerydslRepository {
}