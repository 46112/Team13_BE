package com.theocean.fundering.domain.post.controller;


import com.theocean.fundering.global.dto.PageResponse;
import com.theocean.fundering.domain.post.dto.PostRequest;
import com.theocean.fundering.domain.post.dto.PostResponse;
import com.theocean.fundering.domain.post.service.PostService;
import com.theocean.fundering.global.jwt.userInfo.CustomUserDetails;
import com.theocean.fundering.global.utils.ApiUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


@RequiredArgsConstructor
@RestController
public class PostController {

    private final PostService postService;

    @GetMapping("/posts")
    public ResponseEntity<?> findAll(@RequestParam(value = "postId", required = false) Long postId, Pageable pageable){
        PageResponse<PostResponse.FindAllDTO> responseDTO = postService.findAll(postId, pageable);
        return ResponseEntity.ok(ApiUtils.success(responseDTO));
    }

    @GetMapping("/posts/{postId}")
    public ResponseEntity<?> findByPostId(@AuthenticationPrincipal CustomUserDetails userDetails, @PathVariable Long postId){
        String memberEmail = null;
        if (userDetails != null)
            memberEmail = userDetails.getEmail();
        PostResponse.FindByPostIdDTO responseDTO = postService.findByPostId(memberEmail, postId);

        return ResponseEntity.ok(ApiUtils.success(responseDTO));

    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @PostMapping("/posts/{postId}/introduction")
    public ResponseEntity<?> postIntroduction(@PathVariable Long postId){
        String introduction = postService.getIntroduction(postId);
        return ResponseEntity.ok(ApiUtils.success(introduction));
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @PostMapping("/posts/write")
    public ResponseEntity<?> writePost(@AuthenticationPrincipal CustomUserDetails userDetails,
                                       @RequestBody PostRequest.PostWriteDTO postWriteDTO,
                                       @RequestPart(value = "thumbnail") MultipartFile thumbnail){
        String writerEmail = userDetails.getEmail();
        postService.writePost(writerEmail, postWriteDTO, thumbnail);
        return ResponseEntity.ok(ApiUtils.success(null));
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @PutMapping("/posts/{postId}/edit")
    public ResponseEntity<?> editPost(@AuthenticationPrincipal CustomUserDetails userDetails,
                                      @PathVariable Long postId,
                                      @RequestBody PostRequest.PostEditDTO postEditDTO,
                                      @RequestPart(value = "thumbnail", required = false) MultipartFile thumbnail){
        Long editedPost = postService.editPost(postId, postEditDTO, thumbnail);
        return ResponseEntity.ok(ApiUtils.success(editedPost));
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @DeleteMapping("/posts/{postId}/delete")
    public ResponseEntity<?> deletePost(@PathVariable Long postId){
        postService.deletePost(postId);
        return ResponseEntity.ok(ApiUtils.success(null));
    }

    @GetMapping("/posts/search")
    public ResponseEntity<?> searchPost(@RequestParam(value = "postId", required = false) Long postId, @RequestParam(value = "keyword") String keyword, Pageable pageable){
        var result = postService.searchPost(postId, keyword, pageable);
        return ResponseEntity.ok(ApiUtils.success(result));
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @PostMapping("/uploadImg")
    public ResponseEntity<?> uploadImage(@RequestPart(value = "image") MultipartFile img){
        String result = postService.uploadImage(img);
        return ResponseEntity.ok(ApiUtils.success(result));
    }
}
