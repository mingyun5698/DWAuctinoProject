package com.example.DWShopProject.controller;

import com.example.DWShopProject.dao.ReviewDto;
import com.example.DWShopProject.exception.ResourceNotFoundException;
import com.example.DWShopProject.service.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reviews")
public class ReviewController {
    @Autowired
    private ReviewService reviewService;

    // 리뷰 생성 엔드포인트
    @PostMapping
    public ResponseEntity<ReviewDto> createReview(@RequestBody ReviewDto reviewDto, @AuthenticationPrincipal UserDetails userDetails) {
        ReviewDto createdReview = reviewService.createReview(reviewDto, userDetails.getUsername());
        return ResponseEntity.status(HttpStatus.CREATED).body(createdReview);
    }

    // 리뷰 ID로 리뷰 조회 엔드포인트
    @GetMapping("/{id}")
    public ResponseEntity<ReviewDto> getReviewById(@PathVariable Long id) {
        ReviewDto reviewDto = reviewService.getReviewById(id)
                .orElseThrow(() -> new ResourceNotFoundException("리뷰를 찾을 수 없습니다."));
        return ResponseEntity.ok(reviewDto);
    }

    // 특정 상품에 대한 리뷰 목록 조회 엔드포인트
    @GetMapping("/product/{productId}")
    public ResponseEntity<List<ReviewDto>> getReviewsByProductId(@PathVariable Long productId) {
        List<ReviewDto> reviews = reviewService.getReviewsByProductId(productId);
        return ResponseEntity.ok(reviews);
    }

    // 리뷰 업데이트 엔드포인트
    @PutMapping("/{id}")
    public ResponseEntity<ReviewDto> updateReview(@PathVariable Long id, @RequestBody ReviewDto reviewDto, @AuthenticationPrincipal UserDetails userDetails) {
        ReviewDto updatedReview = reviewService.updateReview(id, reviewDto, userDetails.getUsername());
        return ResponseEntity.ok(updatedReview);
    }

    // 리뷰 삭제 엔드포인트
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReview(@PathVariable Long id, @AuthenticationPrincipal UserDetails userDetails) {
        reviewService.deleteReview(id, userDetails.getUsername());
        return ResponseEntity.noContent().build();
    }
}
