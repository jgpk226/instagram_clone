package com.instagram.instagram.infra.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // 페이지 보안 설정
        http.authorizeRequests()
                .mvcMatchers("/", "/account/sign-up").permitAll()
                .anyRequest().authenticated();

        // login 과 logout 은 post 요청으로 자동 처리
        http.formLogin().loginPage("/login").permitAll();
        http.logout().logoutSuccessUrl("/");
    }

    @Override
    public void configure(WebSecurity web) {
        // static resource 는 security 제외
        web.ignoring().mvcMatchers("/css/**", "/js/**", "/data/**", "/imgs/**");
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
