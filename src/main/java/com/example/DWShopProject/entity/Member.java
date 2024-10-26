package com.example.DWShopProject.entity;

import com.example.DWShopProject.security.MemberRoleEnum;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;


@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "member")  // 테이블명을 명시적으로 지정
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long id;

    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private MemberRoleEnum memberType;

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Order> orders;

    private String memberId;
    private String memberName;

    @OneToOne(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    private Cart cart;

    @Builder
    public Member(MemberRoleEnum memberType, String memberId, String memberName, String password,
                  String birthdate, String gender, String email, String contact) {
        this.memberType = memberType;
        this.memberId = memberId;
        this.memberName = memberName;
        this.password = password;
        this.birthdate = birthdate;
        this.gender = gender;
        this.email = email;
        this.contact = contact;
    }

    @JsonIgnore
    private String password;

    private String birthdate;
    private String gender;
    private String email;
    private String contact;
    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Address> addresses = new ArrayList<>();


    public void updateMemberInfo(String memberName, String password, String birthdate, String gender, String email, String contact) {
        if (memberName != null) this.memberName = memberName;
        if (password != null) this.password = password;
        if (birthdate != null) this.birthdate = birthdate;
        if (gender != null) this.gender = gender;
        if (email != null) this.email = email;
        if (contact != null) this.contact = contact;
    }

    public void updateMemberType(MemberRoleEnum memberType) {
        if (memberType != null) {
            this.memberType = memberType;
        }
    }

    public Member(Long id, MemberRoleEnum memberType, String memberId, String memberName, String password, String birthdate, String gender, String email, String contact) {
        this.id = id;
        this.memberType = memberType;
        this.memberId = memberId;
        this.memberName = memberName;
        this.password = password;
        this.birthdate = birthdate;
        this.gender = gender;
        this.email = email;
        this.contact = contact;

    }
}
