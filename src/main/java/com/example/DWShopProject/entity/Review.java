package com.example.DWShopProject.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @ManyToOne
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @Column(columnDefinition = "TEXT")
    private String content;

    private int rating;

    private LocalDateTime createdDate;

    // 리뷰 내용을 업데이트하는 메서드 (수정 기능이 있을 경우 사용)
    public void updateReview(String content, Integer rating) {
        if (content != null) this.content = content;
        if (rating != null) this.rating = rating;
    }
}
