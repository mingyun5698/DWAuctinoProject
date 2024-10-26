package com.example.DWShopProject.controller;

import com.example.DWShopProject.dao.AddressDto;
import com.example.DWShopProject.entity.Address;
import com.example.DWShopProject.jwt.JwtUtil;
import com.example.DWShopProject.security.MemberDetailsImpl;
import com.example.DWShopProject.service.AddressService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@Slf4j
@RestController
@RequestMapping("/api/addresses")
public class AddressController {
    @Autowired
    private AddressService addressService;
    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/add")
    public ResponseEntity<?> addAddress(@AuthenticationPrincipal MemberDetailsImpl memberDetails, @RequestBody AddressDto addressDto) {
        Long memberId = memberDetails.getMember().getId();
        addressService.addAddress(memberId, addressDto);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/list")
    public ResponseEntity<List<AddressDto>> getAddressList(@AuthenticationPrincipal MemberDetailsImpl memberDetails) {
        Long memberId = memberDetails.getMember().getId();
        List<AddressDto> addressList = addressService.getAddressList(memberId);
        return ResponseEntity.ok(addressList);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Void> updateAddress(@AuthenticationPrincipal MemberDetailsImpl memberDetails, @PathVariable Long id, @RequestBody AddressDto addressDto) {
        Long memberId = memberDetails.getMember().getId();
        addressService.updateAddress(memberId, id, addressDto);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteAddress(@AuthenticationPrincipal MemberDetailsImpl memberDetails, @PathVariable Long id) {
        Long memberId = memberDetails.getMember().getId();
        addressService.deleteAddress(memberId, id);
        return ResponseEntity.ok().build();
    }
}




