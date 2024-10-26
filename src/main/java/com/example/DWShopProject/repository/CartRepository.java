package com.example.DWShopProject.repository;

import com.example.DWShopProject.entity.Cart;
import com.example.DWShopProject.entity.CartItem;
import com.example.DWShopProject.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart, Long> {
    Optional<Cart> findByMember(Member member);
    Optional<Cart> findByMemberId(Long memberId);
}
