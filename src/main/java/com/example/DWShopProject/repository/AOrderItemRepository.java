package com.example.DWShopProject.repository;

import com.example.DWShopProject.entity.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AOrderItemRepository extends JpaRepository<OrderItem, Long> {
}