package com.example.DWShopProject.controller;

import com.example.DWShopProject.dao.CartDto;
import com.example.DWShopProject.entity.CartItem;
import com.example.DWShopProject.entity.Member;
import com.example.DWShopProject.jwt.JwtUtil;
import com.example.DWShopProject.repository.CartItemRepository;
import com.example.DWShopProject.security.MemberDetailsImpl;
import com.example.DWShopProject.security.MemberDetailsServiceImpl;
import com.example.DWShopProject.service.CartService;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cart")
public class CartController {
    @Autowired
    private CartService cartService;
    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    MemberDetailsServiceImpl memberDetailsService;

    @Autowired
    CartItemRepository cartItemRepository;

    // 장바구니 조회
    @GetMapping
    public ResponseEntity<?> getMyCart(HttpServletRequest request, @AuthenticationPrincipal MemberDetailsImpl memberDetails) {
//        String token = jwtUtil.resolveToken(request);
//
//        if (token == null || !jwtUtil.validateToken(token)) {
//            return ResponseEntity.status(401).body("Invalid token");
//        }

//        String memberId = jwtUtil.getUserInfoFromToken(token).getSubject();
        List<CartItem> cartItems = cartService.getCartItemsByMemberId(memberDetails.getMember());

        return ResponseEntity.ok(cartItems);
    }
    // 회원 ID로 장바구니 조회
//    @GetMapping("/member/{memberId}")
//    public ResponseEntity<CartDto> getCartByMemberId(@PathVariable Long memberId) {
//        CartDto cartDto = cartService.getCartByMemberId(memberId);
//        return ResponseEntity.ok(cartDto);
//    }

    // 장바구니 생성
//    @PostMapping
//    public ResponseEntity<CartDto> createCart(@RequestParam Long memberId) {
//        CartDto cartDto = cartService.createCart(memberId);
//        return ResponseEntity.ok(cartDto);
//    }

    // 장바구니에 아이템 추가
    @PostMapping("/items")
    public ResponseEntity<CartDto> addItemToCart(HttpServletRequest request, @RequestParam Long productId, @AuthenticationPrincipal MemberDetailsImpl memberDetails) {

//        String memberId = jwtUtil.getMemberIdFromToken(token);
        // 토큰에서 추출된 memberId를 사용하여 CartService에 전달
        CartDto cartDto = cartService.addItemToCart(memberDetails.getMember(), productId);
        return ResponseEntity.ok(cartDto);
    }

    // 장바구니에서 아이템 제거
    @DeleteMapping("/items")
    public ResponseEntity<Void> removeItemFromCart(HttpServletRequest request, @RequestParam Long productId) {
        String token = jwtUtil.resolveToken(request);
        System.out.println("Token: " + token);

        if (token == null || !jwtUtil.validateToken(token)) {
            System.out.println("Invalid or missing token.");
            return ResponseEntity.status(401).build();
        }

        // 토큰에서 `memberId`를 추출
        String memberId = jwtUtil.getMemberIdFromToken(token);

        try {
            // `memberId`를 사용하여 `Member` 엔티티를 조회하고, `id`를 가져옴
            Member member = memberDetailsService.loadMemberByUsername(memberId);
            cartService.removeItemFromCart(member.getId(), productId);
            System.out.println("Item removed from cart.");
        } catch (Exception e) {
            System.out.println("Error removing item from cart: " + e.getMessage());
            return ResponseEntity.status(500).build();
        }

        return ResponseEntity.ok().build();
    }

    // 장바구니에서 특정 상품을 모두 제거
//    @DeleteMapping("/{id}/items/{productId}")
//    public ResponseEntity<CartDto> removeAllItemsFromCart(@PathVariable Long id, @PathVariable Long productId) {
//        CartDto cartDto = cartService.removeAllItemsFromCart(id, productId);
//        return ResponseEntity.ok(cartDto);
//    }
//
//    // 장바구니에서 아이템 수량 업데이트 (PUT 메서드 추가)
//    @PutMapping("/{id}/items/{productId}")
//    public ResponseEntity<CartDto> updateItemInCart(@PathVariable Long id, @PathVariable Long productId, @RequestParam int quantity) {
//        CartDto cartDto = cartService.updateItemInCart(id, productId, quantity);
//        return ResponseEntity.ok(cartDto);
//    }

    // 모든 장바구니 조회
//    @GetMapping
//    public ResponseEntity<List<CartDto>> getAllCarts() {
//        List<CartDto> cartDtos = cartService.getAllCarts();
//        return ResponseEntity.ok(cartDtos);
//    }

}
