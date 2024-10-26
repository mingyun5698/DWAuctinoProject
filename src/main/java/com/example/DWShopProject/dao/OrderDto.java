package com.example.DWShopProject.dao;

import com.example.DWShopProject.entity.Member;
import com.example.DWShopProject.enums.OrderStatus;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder

public class OrderDto {

    private Long id;
    private MemberDto member;
    private String recipientName;
    private String contactNumber;
    private String deliveryLocation;
    private LocalDateTime createDate;
    private String request;
    private int totalPrice;
    private OrderStatus status;
    private List<OrderItemDto> orderItems;


}
