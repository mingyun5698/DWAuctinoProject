package com.example.DWShopProject.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Builder;

@Builder
@Entity
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String recipientName;
    private String contactNumber;
    private String deliveryLocation;

    @ManyToOne
    @JoinColumn(name = "member_id")
    @JsonBackReference
    private Member member;

    public Address(Long id, String recipientName, String contactNumber, String deliveryLocation, Member member) {
        this.id = id;
        this.recipientName = recipientName;
        this.contactNumber = contactNumber;
        this.deliveryLocation = deliveryLocation;
        this.member = member;
    }

    public Long getId() {
        return id;
    }

    public String getRecipientName() {
        return recipientName;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public String getDeliveryLocation() {
        return deliveryLocation;
    }

    public Member getMember() {
        return member;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setRecipientName(String recipientName) {
        this.recipientName = recipientName;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    public void setDeliveryLocation(String deliveryLocation) {
        this.deliveryLocation = deliveryLocation;
    }

    public void setMember(Member member) {
        this.member = member;
    }

    public Address() {
    }
}
