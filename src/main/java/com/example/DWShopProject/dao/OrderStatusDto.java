package com.example.DWShopProject.dto;

import lombok.Getter;
import lombok.Setter;

// 주문 상태와 그에 해당하는 displayName을 포함하는 DTO 클래스
@Getter
@Setter
public class OrderStatusDto {
    private String status;
    private String displayName;

    public OrderStatusDto(String status, String displayName) {
        this.status = status;
        this.displayName = displayName;
    }
}
