package com.example.DWShopProject.service;

import com.example.DWShopProject.dao.LoginDto;
import com.example.DWShopProject.dao.MemberDto;
import com.example.DWShopProject.entity.Cart;
import com.example.DWShopProject.entity.Member;
import com.example.DWShopProject.jwt.JwtUtil;
import com.example.DWShopProject.repository.CartRepository;
import com.example.DWShopProject.repository.MemberRepository;
import com.example.DWShopProject.security.MemberRoleEnum;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@AllArgsConstructor
public class MemberService {

    @Autowired
    private final MemberRepository memberRepository;
    @Autowired
    private final JwtUtil jwtUtil;
    @Autowired
    private final PasswordEncoder passwordEncoder;
    @Autowired
    private CartRepository cartRepository;



    // 회원가입
    public Member signUp(MemberDto memberDto) {

        Member member = new Member();
        member.setMemberName(memberDto.getMemberName());
        member.setMemberId(memberDto.getMemberId());
        member.setPassword(passwordEncoder.encode(memberDto.getPassword()));
        member.setBirthdate(memberDto.getBirthdate());
        member.setGender(memberDto.getGender());
        member.setEmail(memberDto.getEmail());
        member.setContact(memberDto.getContact());
        member.setMemberType(MemberRoleEnum.USER);

        if (memberRepository.findByMemberId(member.getMemberId()).isPresent()) {
            throw new IllegalArgumentException("이미 존재하는 아이디 입니다: " + member.getMemberId());
        }

        memberRepository.save(member);


        Cart cart = new Cart();
        cart.setMember(member);
        cartRepository.save(cart);



        return member;
    }

    //어드민 생성 (임시)



    public Member createAdmin(MemberDto memberDto) {

        Member member = new Member();
        member.setMemberName(memberDto.getMemberName());
        member.setMemberId(memberDto.getMemberId());
        member.setPassword(passwordEncoder.encode(memberDto.getPassword()));
        member.setBirthdate(memberDto.getBirthdate());
        member.setGender(memberDto.getGender());
        member.setEmail(memberDto.getEmail());
        member.setContact(memberDto.getContact());
        member.setMemberType(MemberRoleEnum.ADMIN); // ADMIN으로 설정

        if (memberRepository.findByMemberId(member.getMemberId()).isPresent()) {
            throw new IllegalArgumentException("이미 존재하는 아이디 입니다: " + member.getMemberId());
        }

        memberRepository.save(member);

        return member;
    }

    // 유저 삭제
    public void deleteMember(Long id) {
        memberRepository.deleteById(id);
    }


    // mypage 수정 완료
    public Member edit(MemberDto memberDto) {
        Member member = new Member();
        member.setId(memberDto.getId());
        member.setMemberId(memberDto.getMemberId());
        member.setPassword(passwordEncoder.encode(memberDto.getPassword()));
        member.setMemberName(memberDto.getMemberName());
        member.setContact(memberDto.getContact());
        member.setEmail(memberDto.getEmail());
        member.setBirthdate(memberDto.getBirthdate());
        return member;

        // 여기서 해당 사용자를 수정하는 로직을 구현
        // 예를 들어, JPA를 사용한다면 해당 사용자를 조회하고 값을 업데이트할 것입니다.
    }

    // MemberDTO를 Member 객체로 변환하는 메서드

    public MemberDto AdminupdateMember(Long id, MemberDto memberDto) {
        Member existingMember = memberRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Member not found"));

        // 회원 타입 업데이트
        if (memberDto.getMemberType() != null) {
            existingMember.updateMemberType(memberDto.getMemberType());
        }

        Member updatedMember = memberRepository.save(existingMember);
        return mapToDTO(updatedMember);
    }


    /*로그인*/
    @Transactional
    public String login(LoginDto loginDto, HttpServletResponse response) {
        Optional<Member> optionalMember = memberRepository.findByMemberId(loginDto.getMemberId());

        if (optionalMember.isEmpty()) {
            throw new IllegalArgumentException("회원이 존재하지 않음");
        }

        Member member = optionalMember.get();

        if (!passwordEncoder.matches(loginDto.getPassword(), member.getPassword())) {

            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }

        // JWT 토큰 생성
        String token = jwtUtil.createToken(member.getMemberId(), member.getMemberType());



        return token; // 토큰 반환
    }

    public MemberDto mapToDTO(Member member) {
        return MemberDto.builder()
                .id(member.getId())
                .memberType(member.getMemberType())
                .memberId(member.getMemberId())
                .memberName(member.getMemberName())
                .password(null) // 비밀번호 필드를 null로 설정
                .birthdate(member.getBirthdate())
                .gender(member.getGender())
                .email(member.getEmail())
                .contact(member.getContact())
                .build();
    }

    //회원가입 이메일 찾기 메소드
    public Optional<Member> findMemberByMemberId(MemberDto memberDto) {
        return memberRepository.findByMemberIdAndEmail(memberDto.getMemberId(), memberDto.getEmail());
    }

    public List<Member> findMembersByEmail(MemberDto memberDto) {
        return memberRepository.findAllByEmail(memberDto.getEmail());
    }

    public void passwordResetByEmail(Member member, int verificationCode) {
        member.setPassword(passwordEncoder.encode(String.valueOf(verificationCode)));
        memberRepository.save(member);
    }


}
