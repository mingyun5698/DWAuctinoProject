package com.example.DWShopProject.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    private int quantity; //구매할 아이템 갯수

    private int price; // 주문할 당시의 가격(변동x)

    public void updateOrderItemInfo(int quantity, int price) {
        if (quantity >= 0) this.quantity = quantity;
        if (price >= 0) this.price = price;
    }

    // 주문 할당 메서드
    public void assignOrder(Order order) {
        this.order = order;
    }

}
