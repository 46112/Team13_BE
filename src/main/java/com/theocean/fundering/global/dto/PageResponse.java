package com.theocean.fundering.global.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.stream.Collectors;

@Getter
public class PageResponse<T> {
    private final List<T> content;
    private final int currentPage;
    private final boolean isLastPage;

    public PageResponse(Slice<T> sliceContent) {
        this.content = sliceContent.getContent();
        this.currentPage = sliceContent.getNumber() + 1;
        this.isLastPage = sliceContent.isLast();
    }
}
