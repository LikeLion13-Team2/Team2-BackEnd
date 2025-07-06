package com.poco.poco_backend.global.security.auth;

import com.poco.poco_backend.domain.member.entity.Member;
import com.poco.poco_backend.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

//클래스에 자동으로 로그(logger)객체를 만들어주는 기능
@Slf4j
@Service
//클래스의 final 필드나 @NotNull 필드만을 사용해서 생성자를 자동으로 만들어줌
@RequiredArgsConstructor
//UserDetailsService를 구현 (커스텀해서 쓰겠다는 것)
public class CustomUserDetailsService implements UserDetailsService {

    private final MemberRepository memberRepository;

    //Username(Email) 로 CustomUserDetail을 가져오기
    //이메일을 받아서 사용자의 정보가 담긴 UserDetails을 반환
    //로그 출력하고
    //Optional -> 해당 내용이 있을 수도 있고, 없을 수도 있는데
    //없어도 오류 띄우지 않게 처리한다는 뜻
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        log.info("[ CustomUserDetailsService ] Email을 이용하여 User 를 검색합니다.");

        Optional<Member> userEntity = memberRepository.findByEmail(email);

        //isPresent -> Optional에 실제로 값이 있는지 없는지 boolean값 반환
        //userEntity안에 값이 있으면~
        //위에서 만든 userEntity에서 값을 필드들을 뽑아냄
        //CustomUserDetails에 뽑아낸 필드들을 집어넣음
        if (userEntity.isPresent()) {
            Member member = userEntity.get();
            return new CustomUserDetails(member.getEmail(),member.getPassword(), member.getRoles());
        }
        throw new UsernameNotFoundException("사용자가 존재하지 않습니다.");

    }
}
