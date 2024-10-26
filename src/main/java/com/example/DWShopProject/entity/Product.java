package com.example.DWShopProject.entity;

import com.example.DWShopProject.enums.ProductTypeEnum;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 직접 데이터 테이블에 외래키 같은 방식으로 연결된 것은 아니고, 그냥 같은 칼럼을 참고만 한다.
    // ManyToOne 같은 방식으로 서로 연결된 것이 아니다.
    @Enumerated(EnumType.STRING)
    private ProductTypeEnum productType;

    private String productName;
    private int price;

    @Column(columnDefinition = "TEXT")
    private String explanation;

    private String imageUrl; // 이미지 URL 필드 추가

    private LocalDateTime createDate;

    public void updateProductInfo(ProductTypeEnum productType, String productName, Integer price, String explanation, String imageUrl) {
        if (productType != null) this.productType = productType;
        if (productName != null) this.productName = productName;
        if (price != null) this.price = price;
        if (explanation != null) this.explanation = explanation;
        if (imageUrl != null) this.imageUrl = imageUrl;
    }
}
