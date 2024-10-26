package com.example.DWShopProject.controller;

import com.example.DWShopProject.dao.AOrderDto;
import com.example.DWShopProject.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/admin/orders")
@Validated
public class AOrderController {

    private final com.example.DWShopProject.service.AOrderService AOrderService;

    @Autowired
    public AOrderController(com.example.DWShopProject.service.AOrderService AOrderService) {
        this.AOrderService = AOrderService;
    }

    // 새로운 주문을 생성하는 엔드포인트
    @PostMapping
    public ResponseEntity<AOrderDto> createOrder(@RequestBody AOrderDto orderDto) {
        com.example.DWShopProject.dao.AOrderDto createdOrder = AOrderService.createOrder(orderDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdOrder);
    }

    // 주문 ID로 특정 주문을 조회하는 엔드포인트
    @GetMapping("/{id}")
    public ResponseEntity<AOrderDto> getOrderById(@PathVariable Long id) {
        AOrderDto orderDto = AOrderService.getOrderById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found"));
        return ResponseEntity.ok(orderDto);
    }

    // 모든 주문을 조회하는 엔드포인트
    @GetMapping
    public ResponseEntity<List<AOrderDto>> getAllOrders() {
        List<AOrderDto> orders = AOrderService.getAllOrders();
        return ResponseEntity.ok(orders);
    }

    // 주문 상태를 업데이트하는 엔드포인트
    @PutMapping("/{id}/status")
    public ResponseEntity<AOrderDto> updateOrderStatus(@PathVariable Long id, @RequestBody Map<String, String> statusMap) {
        String status = statusMap.get("status");
        AOrderDto updatedOrder = AOrderService.updateOrderStatus(id, status);
        return ResponseEntity.ok(updatedOrder);
    }

    // 여러 주문의 상태를 업데이트하는 엔드포인트
    @PutMapping("/status")
    public ResponseEntity<List<AOrderDto>> updateOrderStatuses(@RequestBody Map<String, Object> requestBody) {
        List<Integer> orderIds = (List<Integer>) requestBody.get("orderIds");
        String status = (String) requestBody.get("status");

        // List<Integer>를 List<Long>으로 변환
        List<Long> longOrderIds = orderIds.stream()
                .map(Long::valueOf)
                .collect(Collectors.toList());

        List<AOrderDto> updatedOrders = AOrderService.updateOrderStatuses(longOrderIds, status);
        return ResponseEntity.ok(updatedOrders);
    }

    // 특정 주문 항목의 수량을 업데이트하는 엔드포인트
    @PutMapping("/{orderId}/items/{orderItemId}/quantity")
    public ResponseEntity<AOrderDto> updateOrderItemQuantity(
            @PathVariable Long orderId,
            @PathVariable Long orderItemId,
            @RequestParam int quantity) {
        AOrderDto updatedOrder = AOrderService.updateOrderItemQuantity(orderId, orderItemId, quantity);
        return ResponseEntity.ok(updatedOrder);
    }

    // 특정 주문을 업데이트하는 엔드포인트
    @PutMapping("/{id}")
    public ResponseEntity<AOrderDto> updateOrder(@PathVariable Long id, @RequestBody AOrderDto orderDto) {
        AOrderDto updatedOrder = AOrderService.updateOrder(id, orderDto);
        return ResponseEntity.ok(updatedOrder);
    }

    // 특정 주문을 삭제하는 엔드포인트
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOrder(@PathVariable Long id) {
        AOrderService.deleteOrder(id);
        return ResponseEntity.noContent().build();
    }

    // 여러 주문을 삭제하는 엔드포인트
    @DeleteMapping
    public ResponseEntity<Void> deleteOrders(@RequestBody List<Long> orderIds) {
        AOrderService.deleteOrders(orderIds);
        return ResponseEntity.noContent().build();
    }
}
