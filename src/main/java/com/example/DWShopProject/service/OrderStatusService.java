package com.example.DWShopProject.service;

import com.example.DWShopProject.dto.OrderStatusDto;
import com.example.DWShopProject.enums.OrderStatus;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class OrderStatusService {

    // 모든 주문 상태와 그에 해당하는 displayName을 반환하는 메서드
    public List<String> getAllOrderStatuses() {
        return Arrays.stream(OrderStatus.values())
                .map(OrderStatus::getDisplayName)
                .collect(Collectors.toList());
    }

    // 주어진 상태 코드에 해당하는 displayName을 반환하는 메서드
    public String getOrderStatusDisplayName(String status) {
        Optional<OrderStatus> orderStatus = Arrays.stream(OrderStatus.values())
                .filter(os -> os.name().equalsIgnoreCase(status))
                .findFirst();

        return orderStatus.map(OrderStatus::getDisplayName)
                .orElseThrow(() -> new IllegalArgumentException("Invalid Order Status: " + status));
    }

    // 모든 주문 상태와 displayName을 포함하는 객체를 반환하는 메서드
    public List<OrderStatusDto> getAllOrderStatusesWithDisplayNames() {
        return Arrays.stream(OrderStatus.values())
                .map(status -> new OrderStatusDto(status.name(), status.getDisplayName()))
                .collect(Collectors.toList());
    }

    // 주어진 상태 코드에 해당하는 상태 객체를 반환하는 메서드
    public OrderStatusDto getOrderStatus(String status) {
        return Arrays.stream(OrderStatus.values())
                .filter(os -> os.name().equalsIgnoreCase(status))
                .map(os -> new OrderStatusDto(os.name(), os.getDisplayName()))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Invalid Order Status: " + status));
    }
}
