package com.example.DWShopProject.dao;

import com.example.DWShopProject.entity.Member;
import com.example.DWShopProject.security.MemberRoleEnum;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class MemberDto {
    private Long id;
    private String memberName;
    private String memberId;
    private String password;
    private String birthdate;
    private String gender;
    private String email;
    private String contact;
    private List<AddressDto> addresses;
    private MemberRoleEnum memberType;

    public MemberDto(Member member) {
        this.id = member.getId();
        this.memberName = member.getMemberName();
        this.memberId = member.getMemberId();
        this.password = member.getPassword();
        this.birthdate = member.getBirthdate();
        this.gender = member.getGender();
        this.email = member.getEmail();
        this.contact = member.getContact();
    }
}
