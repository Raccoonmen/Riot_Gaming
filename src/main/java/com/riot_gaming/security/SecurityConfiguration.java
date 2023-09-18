package com.riot_gaming.security;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Slf4j
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
@RequiredArgsConstructor
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

  // WebSecurityCofigurerAdapter를 상속받아서 사용함.
  // EnableWebSecurity, EnableGlobalMethod..,
  // RequiredArgs..어노테이션을 사용한다.

  // Filter를 가져와서 configure, authenticationManagerBean
  // 메서드를 이용한다.

  private final JwtAuthenticationFilter authenticationFilter;


  // configure에서 어떤 것을 허락하는지 정의해준다.
  // RESAPI로 구현하면 disable해줄거 해주고 세션도 STATELESS로 해준다.
  // antMatchers를 통해서 해당 링크들은 접근 무조건 허용해줌.
  // addFilter로 Username... 필터를 추가해준다. Security에서 필터체인에
  // 사용자 정의 인증 필터를 추가한다.
  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http
        .httpBasic().disable()
        .csrf().disable()
        .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        .and()
        .authorizeRequests()
        .antMatchers("/**/signup", "/**/login").permitAll()
        .and()
        .addFilterBefore(this.authenticationFilter,
            UsernamePasswordAuthenticationFilter.class);
  }

  //  게시판 개발할때 제약 없도록 일단 /board 링크들은 전부 허용해줬음.
  @Override
  public void configure(final WebSecurity web) throws Exception {
    web.ignoring()
        .antMatchers("/board/**");
  }


  // spring boot 2.x 부터 이렇게 선언.
  @Bean
  @Override
  public AuthenticationManager authenticationManagerBean() throws Exception {
    return super.authenticationManagerBean();
  }

}
