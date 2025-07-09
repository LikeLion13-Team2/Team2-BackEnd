package com.poco.poco_backend.domain.member.controller;

import com.poco.poco_backend.domain.member.dto.request.CodeDTO;
import com.poco.poco_backend.domain.member.dto.request.GoogleMemberDTO;
import com.poco.poco_backend.domain.member.entity.Member;
import com.poco.poco_backend.domain.member.repository.TokenRepository;
import com.poco.poco_backend.domain.member.service.GoogleLoginService;
import com.poco.poco_backend.global.CustomResponse;
import com.poco.poco_backend.global.security.jwt.JwtDTO;
import com.poco.poco_backend.global.security.jwt.JwtUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.security.SignatureException;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/oauth")
@Tag(name = "구글 로그인 토큰 발급 api", description = "구글 로그인 토큰 발급 api입니다.")
public class GoogleLoginController {

    private final GoogleLoginService googleLoginService;

    private final JwtUtil jwtUtil;
    private final TokenRepository tokenRepository;

    @Operation(summary = "구글 회원가입 및 로그인"
            , description = """
            클라이언트로부터 POST로 전달받은 인가 코드를 이용하여 구글로부터 accessToken을 발급 받습니다.\n
            발급 받은 accessToken에서 사용자의 정보를 추출한 뒤, 서버 accessToken, refreshToken을 발급한 뒤, 
            클라이언트로 전송합니다.            
            """
    )
    @PostMapping("/google")
    public CustomResponse<?> googleLogin(@RequestBody CodeDTO codeDTO) {

        //코드를 보내서 파싱한 다음
        GoogleMemberDTO memberDTO = googleLoginService.getMemberInfo(codeDTO.code());

        Member member = googleLoginService.signupOrGetMember(memberDTO);

        return CustomResponse.onSuccess(googleLoginService.createJwt(member));
    }

    //토큰 재발급 api
    @Operation(summary = "토큰 재발급",
            description = "토큰 재발급. accessToken과 refreshToken을 body에 담아서 전송합니다.")
    @PostMapping("/reissue")
    public CustomResponse<?> reissue(JwtDTO jwtDto) throws SignatureException {

        log.info("[ Google Login Controller ] 토큰을 재발급 합니다.");

        return CustomResponse.onSuccess(googleLoginService.reissueToken(jwtDto));
    }

    @Transactional
    @SecurityRequirement(name = "JWT TOKEN")
    @Operation(summary = "로그아웃", description = "로그아웃 api입니다.")
    @DeleteMapping("/logout")
    public CustomResponse<?> logout(HttpServletRequest request) throws SignatureException {

        String accessToken = jwtUtil.resolveAccessToken(request);

        log.info("[ Google Login Controller ] 토큰으로부터 이메일을 추출합니다..");

        String email = jwtUtil.getEmail(accessToken);

        log.info("[ Google Login Controller ] 로그아웃을 진행합니다.");

        tokenRepository.deleteByEmail(email);

        log.info("[ Google Login Controller ] 로그아웃이 완료 되었습니다..");

        return CustomResponse.onSuccess("로그아웃 되었습니다.");
    }
}
