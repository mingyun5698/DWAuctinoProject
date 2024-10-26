package com.example.DWShopProject.controller;

import com.example.DWShopProject.dao.OrderDto;
import com.example.DWShopProject.entity.Order;
import com.example.DWShopProject.repository.MemberRepository;
import com.example.DWShopProject.repository.OrderRepository;
import com.example.DWShopProject.repository.ProductRepository;
import com.example.DWShopProject.security.MemberDetailsImpl;
import com.example.DWShopProject.service.MemberService;
import com.example.DWShopProject.service.OrderService;
import com.example.DWShopProject.service.PayPalService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/order")
@Slf4j
public class OrderController {

    @Autowired
    private PayPalService payPalService;
    @Autowired
    private OrderService orderService;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private OrderRepository orderRepository;


    //---------------------주문생성 테스트
    @PostMapping("/create")
    public ResponseEntity<?> createOrder(@AuthenticationPrincipal MemberDetailsImpl memberDetails, @RequestBody OrderDto orderDto) {
        Long memberId = memberDetails.getMember().getId();
        orderService.createOrder(memberId, orderDto);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/list")
    public ResponseEntity<List<OrderDto>> getOrderList(@AuthenticationPrincipal MemberDetailsImpl memberDetails) {
        Long memberId = memberDetails.getMember().getId();
        List<OrderDto> orderList = orderService.getOrderListByMemberId(memberId);
        return ResponseEntity.ok(orderList);
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderDto> getOrderDetail(@AuthenticationPrincipal MemberDetailsImpl memberDetails, @PathVariable Long id) {
        Long memberId = memberDetails.getMember().getId();
        OrderDto order = orderService.getOrderDetailByIdAndMemberId(id, memberId);
        return ResponseEntity.ok(order);
    }





}
