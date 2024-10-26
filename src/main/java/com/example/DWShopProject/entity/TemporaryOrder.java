package com.example.DWShopProject.entity;

import com.example.DWShopProject.dao.OrderItemDto;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "temporary_orders")
public class TemporaryOrder implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long memberId;
    private String recipientName;
    private String contactNumber;
    private String deliveryLocation;
    private String request;
    private int totalPrice;

    @Lob
    private String orderItems;

    private LocalDateTime createdAt = LocalDateTime.now();

    // getters and setters

    // 임시로 Gson 객체를 생성해 사용하는 메서드
    public void setOrderItems(List<OrderItemDto> orderItems) {
        this.orderItems = new Gson().toJson(orderItems);
    }

    public List<OrderItemDto> getOrderItems() {
        return new Gson().fromJson(this.orderItems, new TypeToken<List<OrderItemDto>>(){}.getType());
    }
}
