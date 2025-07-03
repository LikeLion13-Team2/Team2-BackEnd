package com.poco.poco_backend.global.security.auth;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class CustomUserDetails implements UserDetails {

    //MemberEntity에 존재하는 내용들이어야함.
    private final String email;

    //일반 로그인을 구현할 경우를 대비한 주석처리
    private final String password;

    private final String roles;

    //생성자
    public CustomUserDetails(String email, String password, String roles) {
        this.email = email;
        this.password = password;
        this.roles = roles;
    }

    //반환값 = 뭐든간에 GrantedAuthority를 구현한(상속받은)게 들어간 Collection
    //authorities라는 리스트를 만들고
    //사용자의 권한을 SimpleGrantedAuthority로 감싸서 리스트에 추가
    //추가된 리스트 반환
    //SimpleGrantedAuthority -> GrantedAuthority의 구현체로, role을 받아 권한의 자격을 부여하는 느낌
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(roles));
        return authorities;
    }

    //원래 UserDetails의 모든 메서드를 모두 오버라이딩 해야함
    //오버라이드 함으로써 서버에 따라 재정의
    @Override
    public String getUsername() {
        return email;
    }


    //일반 로그인을 구현할 경우를 대비해 주석처리
    @Override
    public String getPassword() {
        return password;
    }

    // Account 가 만료되었는지?
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    // Account 가 잠겨있는지?
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    // Credential 만료되지 않았는지?
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    // 활성화가 되어있는지?
    @Override
    public boolean isEnabled() {
        // User Entity 에서 Status 가져온 후 true? false? 검사
        return true;
    }
}
