package com.example.DWShopProject.service;

import com.example.DWShopProject.dao.CartItemDto;
import com.example.DWShopProject.dao.CartDto;
import com.example.DWShopProject.entity.Cart;
import com.example.DWShopProject.entity.CartItem;
import com.example.DWShopProject.entity.Member;
import com.example.DWShopProject.entity.Product;
import com.example.DWShopProject.repository.CartItemRepository;
import com.example.DWShopProject.repository.CartRepository;
import com.example.DWShopProject.repository.MemberRepository;
import com.example.DWShopProject.repository.ProductRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CartService {

    private final CartRepository cartRepository;
    private final ProductRepository productRepository;
    private final MemberRepository memberRepository;

    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    public CartService(CartRepository cartRepository, ProductRepository productRepository, MemberRepository memberRepository) {
        this.cartRepository = cartRepository;
        this.productRepository = productRepository;
        this.memberRepository = memberRepository;
    }

    public CartDto getCart(Long cartId) {
        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new RuntimeException("장바구니를 찾을 수 없습니다."));
        return mapToDto(cart);
    }

//    public CartDto getCartByMemberId(Long memberId) {
//        Cart cart = cartRepository.findByMemberId(memberId)
//                .orElseThrow(() -> new RuntimeException("장바구니를 찾을 수 없습니다."));
//        return mapToDto(cart);
//    }

    public CartDto createCart(Long memberId) {
        Cart newCart = Cart.builder()
                .member(memberRepository.findById(memberId)
                        .orElseThrow(() -> new RuntimeException("회원을 찾을 수 없습니다.")))
                .items(new ArrayList<>())
                .build();
        return mapToDto(cartRepository.save(newCart));
    }

    public CartDto addItemToCart(Member member, Long productId) {
//        Member member = memberRepository.findByMemberId(memberId)
//                .orElseThrow(() -> new RuntimeException("회원이 존재하지 않음"));
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("상품이 존재하지 않음"));

        Cart cart = cartRepository.findByMember(member)
                .orElseGet(() -> cartRepository.save(new Cart(member, new ArrayList<>())));

        CartItem existingItem = cartItemRepository.findByCartAndProduct(cart, product)
                .orElse(null);

        if (existingItem != null) {
            existingItem.updateQuantity(existingItem.getQuantity() + 1);
            cartItemRepository.save(existingItem);
        } else {
            CartItem newItem = new CartItem(cart, product, 1);
            cartItemRepository.save(newItem);
        }

        return convertCartToDto(cart);
    }


    @Transactional
    public void removeItemFromCart(Long memberId, Long productId) {
        // `memberId`를 사용하여 카트를 조회
        Cart cart = cartRepository.findByMemberId(memberId)
                .orElseThrow(() -> new RuntimeException("장바구니를 찾을 수 없습니다."));

        // 카트에서 `productId`를 통해 아이템을 조회
        CartItem item = cart.getItems().stream()
                .filter(cartItem -> cartItem.getProduct().getId().equals(productId))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("장바구니에 해당 상품이 없습니다."));

        // 아이템 삭제
        cart.removeItem(item);
        cartRepository.save(cart);
    }

    public CartDto removeAllItemsFromCart(Long cartId, Long productId) {
        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new RuntimeException("장바구니를 찾을 수 없습니다."));

        cart.getItems().removeIf(item -> item.getProduct().getId().equals(productId));

        return mapToDto(cartRepository.save(cart));
    }

    public CartDto updateItemInCart(Long cartId, Long productId, int quantity) {
        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new RuntimeException("장바구니를 찾을 수 없습니다."));
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("상품을 찾을 수 없습니다."));

        Optional<CartItem> existingItem = cart.getItems().stream()
                .filter(item -> item.getProduct().getId().equals(product.getId()))
                .findFirst();

        if (existingItem.isPresent()) {
            CartItem item = existingItem.get();
            item.updateQuantity(quantity);
        } else {
            CartItem newItem = CartItem.builder()
                    .cart(cart)
                    .product(product)
                    .quantity(quantity)
                    .build();
            cart.addItem(newItem);
        }

        return mapToDto(cartRepository.save(cart));
    }

    public List<CartDto> getAllCarts() {
        return cartRepository.findAll().stream().map(this::mapToDto).collect(Collectors.toList());
    }

    private CartDto mapToDto(Cart cart) {
        return CartDto.builder()
                .id(cart.getId())
                .memberId(cart.getMember().getId())
                .items(cart.getItems())
                .build();
    }


    // 장바구니 추가 하는 메서드
    private CartDto convertCartToDto(Cart cart) {
        return CartDto.builder()
                .id(cart.getId())
                .memberId(cart.getMember().getId()) // 기존 memberId 그대로 사용
                .items(cart.getItems())
                .build();
    }

    private CartItemDto convertCartItemToDto(CartItem cartItem) {
        return CartItemDto.builder()
                .id(cartItem.getId())
                .cartId(cartItem.getCart().getId())
                .productId(cartItem.getProduct().getId())
                .quantity(cartItem.getQuantity())
                .build();
    }


    @Transactional
    public List<CartItem> getCartItemsByMemberId(Member member) {
//        Member member = memberRepository.findByMemberId(memberId)
//                .orElseThrow(() -> new IllegalArgumentException("회원이 존재하지 않습니다."));
        Cart cart = cartRepository.findByMember(member)
                .orElseThrow(() -> new IllegalArgumentException("장바구니가 존재하지 않습니다."));
        return cart.getItems();
    }
}
