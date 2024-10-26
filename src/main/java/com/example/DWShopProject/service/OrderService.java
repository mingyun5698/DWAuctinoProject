package com.example.DWShopProject.service;

import com.example.DWShopProject.dao.MemberDto;
import com.example.DWShopProject.dao.OrderDto;
import com.example.DWShopProject.dao.OrderItemDto;
import com.example.DWShopProject.entity.Member;
import com.example.DWShopProject.entity.Order;
import com.example.DWShopProject.entity.OrderItem;
import com.example.DWShopProject.entity.Product;
import com.example.DWShopProject.enums.OrderStatus;
import com.example.DWShopProject.repository.MemberRepository;
import com.example.DWShopProject.repository.OrderRepository;
import com.example.DWShopProject.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderService {

    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private OrderRepository orderRepository;


    public Order createOrder(Long memberId, OrderDto orderDto) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new RuntimeException("회원이 존재하지 않음"));

        Order order = new Order();
        order.setMember(member);
        order.setRecipientName(orderDto.getRecipientName());
        order.setContactNumber(orderDto.getContactNumber());
        order.setDeliveryLocation(orderDto.getDeliveryLocation());
        order.setRequest(orderDto.getRequest());
        order.setCreateDate(LocalDateTime.now());
        order.setStatus(OrderStatus.PENDING);

        List<OrderItem> orderItems = new ArrayList<>();
        int totalPrice = 0;

        for (OrderItemDto itemDto : orderDto.getOrderItems()) {
            Product product = productRepository.findById(itemDto.getProductId())
                    .orElseThrow(() -> new RuntimeException("상품을 찾을 수 없음"));

            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setProduct(product);
            orderItem.setQuantity(itemDto.getQuantity());
            orderItem.setPrice(product.getPrice());

            totalPrice += product.getPrice() * itemDto.getQuantity();
            orderItems.add(orderItem);
        }

        order.setOrderItems(orderItems);
        order.setTotalPrice(totalPrice);

        return orderRepository.save(order);
    }

    public List<OrderDto> getOrderListByMemberId(Long memberId) {
        List<Order> orders = orderRepository.findByMemberId(memberId);
        return orders.stream()
                .map(order -> {
                    List<OrderItemDto> orderItems = order.getOrderItems().stream()
                            .map(item -> new OrderItemDto(
                                    item.getId(),
                                    item.getProduct().getId(),
                                    item.getProduct().getProductName(),
                                    item.getProduct().getImageUrl(),
                                    item.getQuantity(),
                                    item.getPrice()
                            ))
                            .collect(Collectors.toList());

                    MemberDto memberDto = convertToMemberDto(order.getMember());

                    return new OrderDto(
                            order.getId(),
                            memberDto, // MemberDto 추가
                            order.getRecipientName(),
                            order.getContactNumber(),
                            order.getDeliveryLocation(),
                            order.getCreateDate(),
                            order.getRequest(),
                            order.getTotalPrice(),
                            order.getStatus(), // OrderStatus 그대로 사용
                            orderItems
                    );
                })
                .collect(Collectors.toList());
    }


    //주문 상세보기 서비스

    public OrderDto getOrderDetailByIdAndMemberId(Long orderId, Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new RuntimeException("회원이 존재하지 않음"));

        Order order = orderRepository.findByIdAndMember(orderId, member)
                .orElseThrow(() -> new RuntimeException("주문이 존재하지 않음"));

        return convertToOrderDto(order);
    }

    private OrderDto convertToOrderDto(Order order) {
        List<OrderItemDto> orderItems = order.getOrderItems().stream()
                .map(this::convertToOrderItemDto)
                .collect(Collectors.toList());

        MemberDto memberDto = convertToMemberDto(order.getMember());

        return OrderDto.builder()
                .id(order.getId())
                .recipientName(order.getRecipientName())
                .contactNumber(order.getContactNumber())
                .deliveryLocation(order.getDeliveryLocation())
                .createDate(order.getCreateDate())
                .request(order.getRequest())
                .totalPrice(order.getTotalPrice())
                .status(order.getStatus())
                .orderItems(orderItems)
                .member(memberDto)  // 추가된 부분
                .build();
    }

    private MemberDto convertToMemberDto(Member member) {
        return MemberDto.builder()
                .id(member.getId())
                .memberName(member.getMemberName())
                .contact(member.getContact())
                .email(member.getEmail())
                .build();
    }

    private OrderItemDto convertToOrderItemDto(OrderItem orderItem) {
        return OrderItemDto.builder()
                .id(orderItem.getId())
                .productId(orderItem.getProduct().getId())
                .productName(orderItem.getProduct().getProductName())
                .quantity(orderItem.getQuantity())
                .price(orderItem.getPrice())
                .imageUrl(orderItem.getProduct().getImageUrl())
                .build();
    }
}
