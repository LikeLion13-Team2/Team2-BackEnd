package com.poco.poco_backend.domain.member.controller;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestParam;

public class KakaoLoginController {

    public ResponseEntity<?> kakaoLogin(@RequestParam("code") String accessCode,
                                        HttpServletResponse httpServletResponse) {

    }

}
