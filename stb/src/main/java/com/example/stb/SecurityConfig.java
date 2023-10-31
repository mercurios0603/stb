package com.example.stb;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

// 로그인 하지 않은 상태에서 "질문 등록" 버튼을 누르면 "로그인" 화면으로 이동한다.
// 그리고 로그인을 진행하면 원래 하려고 했던 "질문 등록" 화면으로 이동한다.
// 이것은 로그인 후에 원래 하려고 했던 페이지로 리다이렉트 시키는 스프링 시큐리티의 기능이다.

@EnableMethodSecurity(prePostEnabled = true)
@Configuration
@EnableWebSecurity
public class SecurityConfig {

  @Bean
  SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

              // 콤마(.) 이후로 동떨어져 보이는 코드 작성을 체이닝(Chaining)이라고 한다.
              // 체이닝은 메서드 호출을 연속적으로 연결하는 방식으로 설정 값을 구성하고 빌더 패턴을 지원한다.
      http
              .authorizeHttpRequests((authorizeHttpRequests) -> authorizeHttpRequests
              .requestMatchers(new AntPathRequestMatcher("/**")).permitAll())

              // 로그인 설정
              .formLogin((formLogin) -> formLogin
                      .loginPage("/member/login")
                      .defaultSuccessUrl("/"))

              // 로그아웃 설정
              // invalidateHttpSession인 것을 보니 스프링부트 시큐리티에서 자동으로 세션이 생성되고 해제가 되는듯?
              .logout((logout) -> logout
                      .logoutRequestMatcher(new AntPathRequestMatcher("/member/logout"))
                      .logoutSuccessUrl("/")
                      .invalidateHttpSession(true))
    ;
    return http.build();
  }

  @Bean
  PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  // AuthenticationManager는 스프링 시큐리티의 인증을 담당한다.
  // AuthenticationManager는 사용자 인증시 앞에서 작성한 MemberSecurityService와 PasswordEncoder를 사용한다.
  @Bean
  AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
    return authenticationConfiguration.getAuthenticationManager();
  }
}
