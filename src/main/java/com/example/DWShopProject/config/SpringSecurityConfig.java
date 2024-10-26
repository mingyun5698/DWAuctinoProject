package com.example.DWShopProject.config;


import com.example.DWShopProject.jwt.JwtAuthFilter;
import com.example.DWShopProject.jwt.JwtUtil;
import com.example.DWShopProject.security.MemberRoleEnum;
import lombok.AllArgsConstructor;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@AllArgsConstructor
public class SpringSecurityConfig {

    private final JwtUtil jwtUtil;

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        // resources 자원 접근 허용
        return (web) -> web.ignoring()
                .requestMatchers(PathRequest.toStaticResources().atCommonLocations());
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        /* csrf 설정 해제. */
        http
                .csrf(csrf -> csrf
                        .ignoringRequestMatchers("/h2-console/**", "/**") // H2 콘솔에 대한 CSRF 보호 비활성화
                )
                .headers(headers -> headers
                        .frameOptions(frameOptions -> frameOptions.sameOrigin()) // H2 콘솔을 위해 X-Frame-Options 설정
                )
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/h2-console/**" ,"/api/**", "/**", "/api/carts/**", "/api/login").permitAll() // H2 콘솔 접근 허용
                        .requestMatchers("/api/mypage").authenticated() // 모든 로그인한 사용자에게 허용
                        .requestMatchers("/api/adminButton").hasRole("ADMIN") // ADMIN만 허용
                        .anyRequest().authenticated() // 그 외의 모든 요청은 인증 필요
                )
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.ALWAYS) // 세션 관리 방식 설정
                )
                .addFilterBefore(new JwtAuthFilter(jwtUtil), UsernamePasswordAuthenticationFilter.class) // JWT 필터 추가
                .exceptionHandling(exceptionHandling -> exceptionHandling
                        .accessDeniedPage("/forbidden") // 인가(권한 인증) 실패 시 핸들러 설정
                );

        return http.build();
    }
}
