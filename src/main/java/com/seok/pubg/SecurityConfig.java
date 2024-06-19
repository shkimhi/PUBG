package com.seok.pubg;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .antMatchers("/css/**", "/js/**", "/images/**", "/login").permitAll() // 로그인 페이지와 정적 리소스 접근 허용
                .anyRequest().authenticated() // 다른 모든 요청은 인증 필요
                .and()
                .formLogin()
                .loginPage("/login") // 커스텀 로그인 페이지 경로
                .permitAll()
                .and()
                .logout()
                .permitAll();
    }
}
