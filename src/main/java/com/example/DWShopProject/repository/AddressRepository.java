package com.example.DWShopProject.repository;

import com.example.DWShopProject.entity.Address;
import com.example.DWShopProject.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AddressRepository extends JpaRepository<Address, Long> {
    List<Address> findByMember(Member member);
}
