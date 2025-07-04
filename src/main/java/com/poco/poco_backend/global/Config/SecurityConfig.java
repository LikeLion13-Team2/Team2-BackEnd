package com.poco.poco_backend.global.Config;

import com.poco.poco_backend.global.security.filter.JwtAuthorizationFilter;
import com.poco.poco_backend.global.security.handler.JwtAccessDeniedHandler;
import com.poco.poco_backend.global.security.handler.JwtAuthenticationEntryPoint;
import com.poco.poco_backend.global.security.jwt.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HttpBasicConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

//해당 클래스는 스프링 설정 클래스임
@Configuration
//스프링 시큐리티 필터 체인 활성화
@EnableWebSecurity
//생성자 주입을 자동으로 생성
@RequiredArgsConstructor
public class SecurityConfig {

    //인증 관리자 설정
    private final AuthenticationConfiguration authenticationConfiguration;
    //토큰 생성, 파싱, 검증할 객체 생성
    private final JwtUtil jwtUtil;
    //인가 예외 핸들러
    private final JwtAccessDeniedHandler jwtAccessDeniedHandler;
    //인증 실패 핸들러
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;;


    //인증이 필요하지 않은 url
    private final String[] allowUrl = {
            "/api/v1/login", //로그인 은 인증이 필요하지 않음
            "/api/v1/auth", // 회원가입은 인증이 필요하지 않음
            "/api/v1/login/kakao",
            "/auth/reissue", // 토큰 재발급은 인증이 필요하지 않음
            "/auth/**",
            "api/usage",
            "/swagger-ui/**",   // swagger 관련 URL
            "/v3/api-docs/**",
            "/callback/kakao",
            "/api/v1/emails"
    };

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
        //로그인 필터 생성: 사용자 인증 성공 시 jwt 발급
        CustomLoginFilter loginFilter = new CustomLoginFilter(
                authenticationManager(authenticationConfiguration),
                jwtUtil
        );
        //요청 url이 해당 url 일 때, 이 filter가 처리
        loginFilter.setFilterProcessesUrl("/api/v1/login");

        http
                //url 접근 권한 설정
                .authorizeHttpRequests(request -> request
                        //인증 없이 접근 허용
                        .requestMatchers(allowUrl).permitAll()
                        //그 외는 인증 필요
                        .anyRequest().authenticated())
                //jwt인증 필터 등록 -> 매 요청마다 jwt 유효성 검사하겠음
                .addFilterBefore(new JwtAuthorizationFilter(jwtUtil), UsernamePasswordAuthenticationFilter.class)
                //로그인 필터 등록 -> 아이디, 비밀번호 검증 후 jwt 발급
                .addFilterAt(loginFilter, UsernamePasswordAuthenticationFilter.class)
                //기본 로그인 페이지 비활성화 (우리는 restapi로 처리하겠다.)
                .formLogin(AbstractHttpConfigurer::disable)
                //http basic 인증 비활성화
                .httpBasic(HttpBasicConfigurer::disable)
                //csrf보호 비활성화
                .csrf(AbstractHttpConfigurer::disable)
                //인증, 인가 실패에 대한 예외 핸들링 설정
                .exceptionHandling(exceptionHandling -> exceptionHandling
                        .accessDeniedHandler(jwtAccessDeniedHandler)
                        .authenticationEntryPoint(jwtAuthenticationEntryPoint))
        ;

        //최종 SecurityFilterChain 객체 생성
        return http.build();
    }

    @Bean
    //authenticationManager등록
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }


    @Bean
    //비밀번호 암호화를 위한 객체
    public BCryptPasswordEncoder passwordEncoder(){return new BCryptPasswordEncoder();}
}
