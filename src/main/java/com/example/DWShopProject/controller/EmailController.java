package com.example.DWShopProject.controller;


import com.example.DWShopProject.dao.MemberDto;
import com.example.DWShopProject.entity.Member;
import com.example.DWShopProject.repository.MemberRepository;
import com.example.DWShopProject.service.EmailService;
import com.example.DWShopProject.service.MemberService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@Slf4j
@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api")

public class EmailController {
    @Autowired
    private MemberService memberService;
    @Autowired
    private EmailService emailService;
    @Autowired
    private MemberRepository memberRepository;


    Random random = new Random();
    int MAX_EMAIL_ASSOCIATED_ACCOUNTS = 1; // 이메일 하나당 만들수 있느 계정의 수
    int verificationCode = random.nextInt(888888) + 111111;


    //회원가입시 이메일 인증번호 발송
//    @PostMapping("/email")
//    public ResponseEntity<?> emailAuth(@RequestBody MemberDto memberDto) {
//
//
//        List<Member> members = memberRepository.findAllByEmail(memberDto.getEmail());
//        if (members.size() > MAX_EMAIL_ASSOCIATED_ACCOUNTS-1) {
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("하나의 이메일에 하나의 계정만 만들 수 있습니다.");
//        }
//        else
//            emailService.sendVerificationCode(memberDto.getEmail(), verificationCode); // 인증번호 메일보내기
//        return ResponseEntity.ok(verificationCode);
//
//
//    }
//
//
//    // 이메일로 아이디 찾기
//    @PostMapping("/findid")
//    public ResponseEntity<String> findMemberByEmail(@RequestBody MemberDto memberDto) {
//        List<Member> members = memberService.findMembersByEmail(memberDto);
//
//        if (!members.isEmpty()) {
//            emailService.sendEmailWithMemberIds(memberDto.getEmail(), members); // 아이디 메일 보내기
//            return ResponseEntity.ok("이메일로 아이디를 보냈습니다.");
//        } else {
//            // 아이디가 존재하지 않을 경우 Bad Request 반환
//            return ResponseEntity.badRequest().body("아이디가 존재하지 않습니다.");
//        }
//    }
//
//    //비밀번호 찾기
//    @PostMapping("/findpassword")
//    ResponseEntity<String> findMemberByMemberId(@RequestBody MemberDto memberDto) {
//        Optional<Member> optionalMember = memberService.findMemberByMemberId(memberDto);
//
//        if(optionalMember.isPresent()) {
//            memberService.passwordResetByEmail(optionalMember.get(), verificationCode);
//            emailService.sendEmailWithMemberPassword(memberDto.getEmail(), verificationCode);
//
//            return ResponseEntity.ok("메일로 비밀번호가 보내졌습니다.");
//
//        } return ResponseEntity.badRequest().body("아이디 또는 이메일이 존재하지 않습니다.");
//
//    }



}
