package com.example.DWShopProject.dao;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class ReviewDto {
    private Long id;
    private Long productId;
    private Long memberId;
    private String content;
    private int rating;
    private LocalDateTime createdDate;
    private String memberName;
}
