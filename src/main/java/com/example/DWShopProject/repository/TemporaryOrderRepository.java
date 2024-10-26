package com.example.DWShopProject.repository;

import com.example.DWShopProject.entity.TemporaryOrder;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TemporaryOrderRepository extends JpaRepository<TemporaryOrder,Long> {
//    Optional<TemporaryOrder> findByIdAndMemberId(Long id);
    void deleteById(Long id);

}
