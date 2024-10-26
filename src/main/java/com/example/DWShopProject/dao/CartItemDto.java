package com.example.DWShopProject.dao;

import lombok.*;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Setter

public class CartItemDto {
    private Long id;
    private Long cartId;
    private Long productId;
    private int quantity;
}
