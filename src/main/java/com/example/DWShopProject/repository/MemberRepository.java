package com.example.DWShopProject.repository;

import com.example.DWShopProject.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByMemberId(String memberId);

    List<Member> findAllByEmail(String email);

    Optional<Member> findByMemberIdAndEmail(String memberId, String email);
}
