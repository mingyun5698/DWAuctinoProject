package com.example.DWShopProject.repository;

import com.example.DWShopProject.entity.Member;
import com.example.DWShopProject.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long> {

    List<Order> findByMemberId(Long memberId);
    Optional<Order> findByIdAndMember(Long id, Member member);

}
