package com.example.DWShopProject.service;

import com.example.DWShopProject.dao.AddressDto;
import com.example.DWShopProject.entity.Address;
import com.example.DWShopProject.entity.Member;
import com.example.DWShopProject.repository.AddressRepository;
import com.example.DWShopProject.repository.MemberRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j

public class AddressService {
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private AddressRepository addressRepository;
    public void addAddress(Long memberId, AddressDto addressDto) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new RuntimeException("회원이 존재하지 않음"));

        Address address = Address.builder()
                .recipientName(addressDto.getRecipientName())
                .contactNumber(addressDto.getContactNumber())
                .deliveryLocation(addressDto.getDeliveryLocation())
                .member(member)
                .build();

        log.info("Saving address: {}", address);

        addressRepository.save(address);
    }

    public List<AddressDto> getAddressList(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new RuntimeException("회원이 존재하지 않음"));

        List<Address> addresses = addressRepository.findByMember(member);
        return addresses.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public void updateAddress(Long memberId, Long addressId, AddressDto addressDto) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new RuntimeException("회원이 존재하지 않음"));

        Address address = addressRepository.findById(addressId)
                .orElseThrow(() -> new RuntimeException("주소를 찾을 수 없습니다."));

        if (!address.getMember().getId().equals(memberId)) {
            throw new RuntimeException("권한이 없습니다.");
        }

        address.setRecipientName(addressDto.getRecipientName());
        address.setContactNumber(addressDto.getContactNumber());
        address.setDeliveryLocation(addressDto.getDeliveryLocation());

        addressRepository.save(address);
    }

    public void deleteAddress(Long memberId, Long addressId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new RuntimeException("회원이 존재하지 않음"));

        Address address = addressRepository.findById(addressId)
                .orElseThrow(() -> new RuntimeException("주소를 찾을 수 없습니다."));

        if (!address.getMember().getId().equals(memberId)) {
            throw new RuntimeException("권한이 없습니다.");
        }

        addressRepository.delete(address);
    }

    private AddressDto convertToDto(Address address) {
        return AddressDto.builder()
                .recipientName(address.getRecipientName())
                .contactNumber(address.getContactNumber())
                .deliveryLocation(address.getDeliveryLocation())
                .build();
    }
}
