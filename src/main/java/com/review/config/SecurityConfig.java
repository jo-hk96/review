package com.review.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;


@Configuration
public class SecurityConfig {
	@Bean
	  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
	 
			
			// 1. CSRF 보호 기능 비활성화 (API 서버나 간단한 앱에서 사용, 보안상 권장되지는 않음)
		    http
		        .csrf((csrfConfig) ->
		            csrfConfig.disable()
		        )
		        // 2. HTTP 요청에 대한 인가(Authorization) 설정 시작
		        .authorizeHttpRequests(authorizeRequests ->
		            authorizeRequests
		            	//로그인 없이 모두 허용할 경로 정의
		                .requestMatchers("/",
		                		"/css/**","/js/**",
		                		"/detail/**",
				                "/UserJoinForm",
		                        "/UserJoin","/MoviesList","/TopRate"
				                ).permitAll()
		                //ROLE_ADMIN만 허용 시큐리티에서 자동으로 ROLE_ 을 앞에 붙혀줌
		                //hasAuthority 를 붙이면 ROLE_ 접두사를 붙이지 않음
		                .requestMatchers("/Admin/**").hasAnyRole("ADMIN")
		                //위의 명시되지않은 모든 나머지요청은 로그인이 필요함
		                .anyRequest().authenticated()
		        ) 
		        
		        //권한 없이 접근시
		        .exceptionHandling(exception -> exception
		        .accessDeniedPage("/access-error") 
		       );
		 
		    
		    
		    //로그인 페이지 처리
		    http
		        .formLogin(login -> login
		          .loginPage("/UserLoginForm") // 로그인 페이지
		          .loginProcessingUrl("/UserLogin") //로그인 데이터 처리할 경로
		          .usernameParameter("email")
		          .passwordParameter("password")
		          .defaultSuccessUrl("/" , true) // 로그인 성공시
		          .failureForwardUrl("/UserLoginForm?error") // 로그인 실패시
		          .permitAll()
		        );
		    http
	            .logout(logout -> logout
	                    .logoutUrl("/logout") //로그아웃 경로
	                    .logoutSuccessUrl("/") //로그아웃 후 이동할 페이지
	                    .invalidateHttpSession(true)
	                    .deleteCookies("JSESSIONID")
	              );

		// http.build()를 붙여서 SecurityFilterChain 빈으로 반환
	    return http.build();
	  }
	 
	  // 패스워드 암호화로 사용할 bean
	  @Bean
	  public PasswordEncoder getPasswordEncoder() {
		return new BCryptPasswordEncoder();
	  }
	 
	}