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
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;


    //인증이 필요하지 않은 url
    private final String[] allowUrl = {
            "/api/login", //로그인 은 인증이 필요하지 않음
            "/api/oauth/**", // 회원가입은 인증이 필요하지 않음
            "/auth/reissue", // 토큰 재발급은 인증이 필요하지 않음
            "/auth/**", //기타 인증 관련 경로
            "/api/usage", //사용량 측정
            "/swagger-ui/**",   // swagger 관련 URL
            "/v3/api-docs/**", // swagger api 문서
            "/callback/kakao",  //소셜 로그인 콜백
            "/api/v1/emails" //이메일 관련 api
    };

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{

        http
                //요청 url별  접근 권한 설정
                .authorizeHttpRequests(request -> request
                        //인증 없이 접근 허용
                        .requestMatchers(allowUrl).permitAll()
                        //그 외는 인증 필요
                        .anyRequest().authenticated())
                //jwt인증 필터를 UsernamePasswordAuthenticationFilter 앞에 등록 -> 매 요청마다 jwt 유효성 검사하겠음
                .addFilterBefore(new JwtAuthorizationFilter(jwtUtil), UsernamePasswordAuthenticationFilter.class)
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
                // cors
                .cors(cors -> cors.configurationSource(CorsConfig.apiConfigurationSource()))
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
