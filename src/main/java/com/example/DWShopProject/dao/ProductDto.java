package com.example.DWShopProject.dao;

import com.example.DWShopProject.enums.ProductTypeEnum;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor

public class ProductDto {
    private Long id;
    private ProductTypeEnum productType;
    private String productName;
    private int price;
    private String explanation;
    private String image;
}