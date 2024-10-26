package com.example.DWShopProject.controller;
import com.example.DWShopProject.ResourceNotFoundException;
import com.example.DWShopProject.dao.LoginDto;
import com.example.DWShopProject.dao.MemberDto;
import com.example.DWShopProject.entity.Member;
import com.example.DWShopProject.jwt.JwtUtil;
import com.example.DWShopProject.repository.MemberRepository;
import com.example.DWShopProject.security.MemberDetailsImpl;
import com.example.DWShopProject.service.MemberService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/api")
// @CrossOrigin("http://localhost:3000/")
public class MemberRestController {
    private final MemberService memberService;
    private final MemberRepository memberRepository;

    @Autowired
    private JwtUtil jwtUtil;

    public MemberRestController(MemberService memberService, MemberRepository memberRepository) {
        this.memberService = memberService;
        this.memberRepository = memberRepository;
    }

    @GetMapping("/user/type")
    public ResponseEntity<?> getUserType(HttpServletRequest request) {
        String token = jwtUtil.resolveToken(request);
        if (token == null || !jwtUtil.validateToken(token)) {
            return ResponseEntity.status(401).body("Invalid token");
        }

        String memberType = jwtUtil.getUserTypeFromToken(token);
        System.out.println("User Type from Token: " + memberType); // 클레임에서 가져온 멤버 타입 로그

        return ResponseEntity.ok(Collections.singletonMap("memberType", memberType)); // memberType으로 반환
    }

    /*회원가입 API*/
    @PostMapping("/signup")
    public ResponseEntity<?> signUp(@RequestBody MemberDto memberDto) {

        Optional<Member> optionalMember = memberRepository.findByMemberId(memberDto.getMemberId());
        List<Member> members = memberRepository.findAllByEmail(memberDto.getEmail());

        try {
            if (optionalMember.isPresent()) {
                return ResponseEntity.status(HttpStatus.CONFLICT).build();
            } else if (members.size() > new EmailController().MAX_EMAIL_ASSOCIATED_ACCOUNTS-1) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("하나의 이메일에 " + new EmailController().MAX_EMAIL_ASSOCIATED_ACCOUNTS +"개의 아이디만 만들 수 있습니다. ");
            }

            Member member = memberService.signUp(memberDto);
            return ResponseEntity.ok(member);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("회원가입 중 오류가 발생했습니다. 다시 시도해주세요.");
        }
    }

    @PostMapping("/idcheck")
    public ResponseEntity<?> IdCheck(@RequestBody MemberDto memberDto) {
        Optional<Member> optionalMember = memberRepository.findByMemberId(memberDto.getMemberId());
        System.out.println(memberDto.getMemberId());
        if (optionalMember.isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        } return ResponseEntity.ok().build();
    }

    @GetMapping("/members")
    public ResponseEntity<List<MemberDto>> getAllMembers() {
        List<Member> members = memberRepository.findAll();
        List<MemberDto> memberDtos = members.stream()
                .map(memberService::mapToDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(memberDtos);
    }

    @GetMapping("/members/{id}")
    public ResponseEntity<MemberDto> getMemberById(@PathVariable Long id) {
        Member member = memberRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Member not found with id: " + id));
        return ResponseEntity.ok(memberService.mapToDTO(member));
    }

    @PutMapping("/members/{id}")
    public ResponseEntity<MemberDto> updateMember(@PathVariable Long id, @RequestBody MemberDto memberDto) {
        System.out.println("업데이트컨트롤러실행");
        log.info("컨트롤러 DTO:{}", memberDto);

        MemberDto member = memberService.AdminupdateMember(id, memberDto);
        if (member != null) {
            return ResponseEntity.ok(member);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // 관리자 전용 회원 삭제 API
    @DeleteMapping("/members/{id}")
    public ResponseEntity<String> deleteMember(@PathVariable Long id) {
        try {
            memberService.deleteMember(id);
            return ResponseEntity.ok("사용자가 성공적으로 삭제되었습니다.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("사용자 삭제 중 오류가 발생했습니다. 다시 시도해주세요.");
        }
    }
    /* 마이페이지 조회 API */
    @GetMapping("/mypage")
    public ResponseEntity<MemberDto> getMyPage(@AuthenticationPrincipal MemberDetailsImpl memberDetails) {
        Member member = memberDetails.getMember(); // MemberDetailsImpl 객체에서 Member 정보 추출
        MemberDto myPageDTO = new MemberDto(member);
        return ResponseEntity.ok(myPageDTO);
    }

    // mypage 회원탈퇴 API 및 header 쿠키삭제
    @DeleteMapping("/mypage")
    public ResponseEntity<String> deleteMyMember(@AuthenticationPrincipal MemberDetailsImpl memberDetails,
                                                 @CookieValue(value = "Authorization", defaultValue = "", required = false) Cookie jwtCookie,
                                                 HttpServletResponse response) {
        memberService.deleteMember(memberDetails.getMember().getId());
        jwtCookie.setValue(null);
        jwtCookie.setMaxAge(0);
        jwtCookie.setPath("/");
        response.addCookie(jwtCookie);
        return ResponseEntity.ok("사용자 계정이 성공적으로 삭제되었습니다.");
    }

    // mypage 회원 수정 API
    @PutMapping("/mypage")
    public ResponseEntity<Member> updateMyMember(@RequestBody MemberDto memberDto,
                                                 @AuthenticationPrincipal MemberDetailsImpl memberDetails,
                                                 @CookieValue(value = "Authorization", defaultValue = "", required = false) Cookie jwtCookie,
                                                 HttpServletResponse response) {
        Member currentMember = memberDetails.getMember();
        if (!currentMember.getId().equals(memberDto.getId())) {
            return ResponseEntity.status(403).build();
        }

        Member member = memberService.edit(memberDto);
        jwtCookie.setValue(null);
        jwtCookie.setMaxAge(0);
        jwtCookie.setPath("/");
        response.addCookie(jwtCookie);

        return ResponseEntity.ok(member);
    }

    /*로그인 API*/
    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginDto loginDao, HttpServletResponse response) {
        try {
            String token = memberService.login(loginDao, response); // 로그인 서비스에서 토큰 생성 및 반환
            return ResponseEntity.ok(token); // 클라이언트에게 토큰을 반환
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("로그인 중 오류가 발생했습니다. 다시 시도해주세요.");
        }
    }

    /*로그아웃 API*/
    @PostMapping("/logout")
    public ResponseEntity<String> logout(@CookieValue(value = "Authorization", defaultValue = "", required = false) Cookie jwtCookie,
                                         HttpServletResponse response) {
        jwtCookie.setValue(null);
        jwtCookie.setMaxAge(0);
        jwtCookie.setPath("/");
        response.addCookie(jwtCookie);

        return ResponseEntity.ok("로그아웃이 성공적으로 완료되었습니다.");
    }
}
