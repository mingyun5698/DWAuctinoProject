package com.example.DWShopProject.controller;

import com.example.DWShopProject.dto.OrderStatusDto;
import com.example.DWShopProject.service.OrderStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/order-statuses")
@RequiredArgsConstructor
public class OrderStatusController {

    private final OrderStatusService orderStatusService;

    // 모든 주문 상태와 그에 해당하는 displayName을 반환하는 엔드포인트
    @GetMapping
    public List<OrderStatusDto> getAllOrderStatuses() {
        return orderStatusService.getAllOrderStatusesWithDisplayNames();
    }

    // 주어진 상태 코드에 해당하는 주문 상태 객체를 반환하는 엔드포인트
    @GetMapping("/{status}")
    public OrderStatusDto getOrderStatusDisplayName(@PathVariable String status) {
        return orderStatusService.getOrderStatus(status);
    }
}
