package com.example.DWShopProject.repository;

import com.example.DWShopProject.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AOrderRepository extends JpaRepository<Order, Long> {
}