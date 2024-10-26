package com.example.DWShopProject.service;

import com.example.DWShopProject.dao.ReviewDto;
import com.example.DWShopProject.entity.Product;
import com.example.DWShopProject.entity.Review;
import com.example.DWShopProject.entity.Member;
import com.example.DWShopProject.exception.ResourceNotFoundException;
import com.example.DWShopProject.repository.ProductRepository;
import com.example.DWShopProject.repository.ReviewRepository;
import com.example.DWShopProject.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class ReviewService {
    private static final Logger logger = LoggerFactory.getLogger(ReviewService.class);

    private final ReviewRepository reviewRepository;
    private final ProductRepository productRepository;
    private final MemberRepository memberRepository;

    @Autowired
    public ReviewService(ReviewRepository reviewRepository, ProductRepository productRepository, MemberRepository memberRepository) {
        this.reviewRepository = reviewRepository;
        this.productRepository = productRepository;
        this.memberRepository = memberRepository;
    }

    // 리뷰 생성 메서드
    public ReviewDto createReview(ReviewDto reviewDto, String username) {
        logger.info("Creating review for product ID: {}", reviewDto.getProductId());

        Product product = productRepository.findById(reviewDto.getProductId())
                .orElseThrow(() -> new ResourceNotFoundException("상품을 찾을 수 없습니다."));

        Member member = memberRepository.findByMemberId(username)
                .orElseThrow(() -> new ResourceNotFoundException("회원을 찾을 수 없습니다."));

        Review review = Review.builder()
                .product(product)
                .member(member)
                .content(reviewDto.getContent())
                .rating(reviewDto.getRating())
                .createdDate(LocalDateTime.now())
                .build();

        Review savedReview = reviewRepository.save(review);
        logger.info("Review saved with ID: {}", savedReview.getId());

        return mapToDTO(savedReview);
    }

    // 특정 상품에 대한 리뷰 목록을 가져오는 메서드
    @Transactional(readOnly = true)
    public List<ReviewDto> getReviewsByProductId(Long productId) {
        logger.info("Fetching reviews for product ID: {}", productId);
        return reviewRepository.findByProductId(productId).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    // 리뷰 ID로 리뷰를 가져오는 메서드
    @Transactional(readOnly = true)
    public Optional<ReviewDto> getReviewById(Long id) {
        logger.info("Fetching review by ID: {}", id);
        return reviewRepository.findById(id)
                .map(this::mapToDTO);
    }

    // 리뷰 업데이트 메서드
    public ReviewDto updateReview(Long id, ReviewDto reviewDto, String username) {
        logger.info("Updating review with ID: {}", id);

        Review review = reviewRepository.findByIdAndMemberId(id, memberRepository.findByMemberId(username).orElseThrow(() -> new ResourceNotFoundException("회원을 찾을 수 없습니다.")).getId())
                .orElseThrow(() -> new ResourceNotFoundException("리뷰를 찾을 수 없습니다."));

        review.updateReview(reviewDto.getContent(), reviewDto.getRating());

        Review updatedReview = reviewRepository.save(review);
        logger.info("Review updated with ID: {}", updatedReview.getId());

        return mapToDTO(updatedReview);
    }

    // 리뷰 삭제 메서드
    public void deleteReview(Long id, String username) {
        logger.info("Deleting review with ID: {}", id);
        Review review = reviewRepository.findByIdAndMemberId(id, memberRepository.findByMemberId(username).orElseThrow(() -> new ResourceNotFoundException("회원을 찾을 수 없습니다.")).getId())
                .orElseThrow(() -> new ResourceNotFoundException("리뷰를 찾을 수 없습니다."));

        reviewRepository.delete(review);
        logger.info("Review deleted with ID: {}", id);
    }

    // 엔티티를 DTO로 변환하는 메서드
    private ReviewDto mapToDTO(Review review) {
        return ReviewDto.builder()
                .id(review.getId())
                .productId(review.getProduct().getId())
                .memberId(review.getMember().getId())
                .content(review.getContent())
                .rating(review.getRating())
                .createdDate(review.getCreatedDate())
                .memberName(review.getMember().getMemberName())
                .build();
    }
}
