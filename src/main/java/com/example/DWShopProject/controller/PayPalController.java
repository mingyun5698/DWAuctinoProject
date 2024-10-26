package com.example.DWShopProject.controller;

import com.example.DWShopProject.dao.OrderDto;
import com.example.DWShopProject.entity.Order;
import com.example.DWShopProject.entity.TemporaryOrder;
import com.example.DWShopProject.jwt.JwtUtil;
import com.example.DWShopProject.repository.TemporaryOrderRepository;
import com.example.DWShopProject.security.MemberDetailsImpl;
import com.example.DWShopProject.service.OrderService;
import com.example.DWShopProject.service.PayPalService;
import com.google.gson.Gson;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.sound.midi.SysexMessage;
import java.io.IOException;
import java.util.*;


@Slf4j
@RestController
@RequestMapping("/api/paypal")
public class PayPalController {
    @Autowired
    private PayPalService payPalService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private TemporaryOrderRepository temporaryOrderRepository;

    private JwtUtil jwtUtil;


    @PostMapping("/createPayment")
    public ResponseEntity<?> createPayment(@AuthenticationPrincipal MemberDetailsImpl memberDetails, @RequestBody OrderDto orderDto) {
        String accessToken = payPalService.getAccessToken();
        log.info("AccessToken: {}", accessToken);

        int totalPrice = orderDto.getTotalPrice();

        // TemporaryOrder 생성 및 저장
        TemporaryOrder temporaryOrder = new TemporaryOrder();
        temporaryOrder.setMemberId(memberDetails.getMember().getId());
        temporaryOrder.setRecipientName(orderDto.getRecipientName());
        temporaryOrder.setContactNumber(orderDto.getContactNumber());
        temporaryOrder.setDeliveryLocation(orderDto.getDeliveryLocation());
        temporaryOrder.setRequest(orderDto.getRequest());
        temporaryOrder.setOrderItems(orderDto.getOrderItems());
        temporaryOrder.setTotalPrice(totalPrice);

        temporaryOrder = temporaryOrderRepository.save(temporaryOrder);
        String finalReturnUrl = "http://localhost:8080/api/paypal/success?tempOrderId=" + temporaryOrder.getId();

        Map<String, Object> paymentResponse = payPalService.createPayment(accessToken, totalPrice,finalReturnUrl);

        if (paymentResponse != null) {
            String approvalUrl = null;
            List<Map<String, String>> links = (List<Map<String, String>>) paymentResponse.get("links");
            for (Map<String, String> link : links) {
                if ("approval_url".equals(link.get("rel"))) {
                    approvalUrl = link.get("href");
                    break;
                }
            }

            log.info("Final approval URL: " + approvalUrl);  // 최종 approvalUrl 확인
            return ResponseEntity.ok(Collections.singletonMap("approvalUrl", approvalUrl));
        } else {
            temporaryOrderRepository.deleteById(temporaryOrder.getId()); // 실패 시 임시 주문 삭제
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Payment creation failed");
        }
    }

    @GetMapping("/success")
    public void success(@RequestParam("paymentId") String paymentId, @RequestParam("PayerID") String payerId, @RequestParam("tempOrderId") Long tempOrderId, HttpServletResponse response) throws IOException {

        String accessToken = payPalService.getAccessToken();
        log.info("Access token retrieved: " + accessToken);

        Map<String, Object> captureResponse = payPalService.capturePayment(accessToken, paymentId, payerId);
        log.info("Capture response: " + captureResponse);

        if (captureResponse != null) {
            TemporaryOrder tempOrder = temporaryOrderRepository.findById(tempOrderId)
                    .orElseThrow(() -> new IllegalArgumentException("Temporary order not found"));

            // OrderDto 생성
            OrderDto orderDto = new OrderDto();
            orderDto.setRecipientName(tempOrder.getRecipientName());
            orderDto.setContactNumber(tempOrder.getContactNumber());
            orderDto.setDeliveryLocation(tempOrder.getDeliveryLocation());
            orderDto.setRequest(tempOrder.getRequest());
            orderDto.setOrderItems(tempOrder.getOrderItems());
            orderDto.setTotalPrice(tempOrder.getTotalPrice());

            Long memberId = tempOrder.getMemberId();

            Order order = orderService.createOrder(memberId, orderDto);

            // 임시 주문 삭제
            temporaryOrderRepository.deleteById(tempOrderId);

            String redirectUrl = "http://localhost:3000/order/detail/" + order.getId();
            String message = "주문이 완료되었습니다.";
            String htmlResponse = "<html><body>"
                    + "<script>"
                    + "alert('" + message + "');"
                    + "window.location.href = '" + redirectUrl + "';"
                    + "</script>"
                    + "</body></html>";

            response.setContentType("text/html; charset=UTF-8");
            response.getWriter().write(htmlResponse);
        } else {
            response.sendRedirect("http://localhost:3000/mypage?error=Error+capturing+payment");
        }
    }

    @GetMapping("/cancel")
    public void paymentCancel(HttpServletResponse response) throws IOException {
        response.sendRedirect("http://localhost:3000/mypage?error=Payment+was+cancelled");
    }


}
