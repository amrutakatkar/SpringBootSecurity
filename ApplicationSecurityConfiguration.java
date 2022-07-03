package com.example.demo.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class ApplicationSecurityConfiguration extends WebSecurityConfigurerAdapter{
	
	private UserDetailsService userDetailsService;

	public ApplicationSecurityConfiguration(UserDetailsService userDetailsService) {
	
		this.userDetailsService = userDetailsService;
	}
	
	@Override
	protected void configure(HttpSecurity httpSecurity) throws Exception {
		// configure authorization rules here
        httpSecurity.cors().disable();
        httpSecurity.csrf().disable();
        httpSecurity
                .authorizeRequests()
                .antMatchers(HttpMethod.GET,"/api/v1/students/**")
                .hasAnyRole("USER", "ADMIN")
                .and()
                .authorizeRequests()
                .antMatchers(HttpMethod.POST,"/api/v1/students/**")
                .hasRole("ADMIN")
           
                .antMatchers(HttpMethod.DELETE,"/api/v1/students/**")
                .hasRole("ADMIN")
                .anyRequest()
                .authenticated()
              
                .and()
                .httpBasic()
               .and()
                /*
                   Set the sessioncreation policy to avoid using cookies for authentication
                   https://stackoverflow.com/questions/50842258/spring-security-caching-my-authentication/50847571
                 */
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

		
		
	}
	
	
	
	 @Override
	 protected void configure(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {
	         //configure users
	         authenticationManagerBuilder
	                 .userDetailsService(this.userDetailsService)
	                 .passwordEncoder(bcryptPasswordEncoder());
	     }

	 @Bean
	public PasswordEncoder bcryptPasswordEncoder() {
		
		return new BCryptPasswordEncoder();
	}


}
