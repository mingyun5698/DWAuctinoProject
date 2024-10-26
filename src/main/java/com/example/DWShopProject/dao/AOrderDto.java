package com.example.DWShopProject.dao;

import com.example.DWShopProject.enums.OrderStatus;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
public class AOrderDto {
    private Long id; // 주문 ID
    private Long memberId; // 회원 ID
    private String recipientName; // 수취인 이름
    private String contactNumber; // 연락처
    private String deliveryLocation; // 배송지
    private LocalDateTime createDate; // 주문 생성 날짜
    private String request; // 요청사항
    private int totalPrice; // 총 가격
    private OrderStatus status; // 주문 상태
    private List<AOrderItemDto> orderItems; // 주문 아이템 목록
}
