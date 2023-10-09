package com.theocean.fundering.domain.post.dto;


import com.theocean.fundering.domain.celebrity.domain.Celebrity;
import com.theocean.fundering.domain.member.domain.Member;
import com.theocean.fundering.domain.post.domain.Post;
import lombok.*;

import java.time.LocalDateTime;

public class PostRequest {
    @Getter
    @Setter
    @NoArgsConstructor
    @ToString
    public static class PostWriteDTO { // 게시글 작성 DTO
        private Long writerId;
        private String writer;
        private Long celebId;
        private String title;
        private String content;
        private String thumbnail;
        private int targetPrice;
        private LocalDateTime deadline;
        private LocalDateTime createdAt;

        public Post toEntity(Member writer, Celebrity celebrity){
            return Post.builder()
                    .writer(writer)
                    .celebrity(celebrity)
                    .title(title)
                    .content(content)
                    .thumbnail(thumbnail)
                    .targetPrice(targetPrice)
                    .deadline(deadline)
                    .build();
        }

        @Builder
        public PostWriteDTO(Long writerId, String writer, Long celebId, String title, String content, String thumbnail, int targetPrice, LocalDateTime deadline){
            this.writer = writer;
            this.writerId = writerId;
            this.celebId = celebId;
            this.title = title;
            this.content = content;
            this.thumbnail = thumbnail;
            this.targetPrice = targetPrice;
            this.deadline = deadline;
            this.createdAt = LocalDateTime.now();
        }

    }

}
