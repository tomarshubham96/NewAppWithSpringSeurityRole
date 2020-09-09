package com.java.NayaApp.Config;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import nz.net.ultraq.thymeleaf.LayoutDialect;

@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	
	@Autowired
	private DataSource dataSource;

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.jdbcAuthentication().dataSource(dataSource)
		.usersByUsernameQuery("select username as principal, password as credentails, true from user where username=?")
		.authoritiesByUsernameQuery("select USERS_USERNAME as principal, ROLES_ROLE_DESC as role from user_roles where users_username=?")
		.passwordEncoder(passwordEncoder()).rolePrefix("ROLE_");  
		
	}
	
	@Bean
	BCryptPasswordEncoder passwordEncoder() {

	    return new BCryptPasswordEncoder();
	}
	
	@Bean
	public LayoutDialect layoutDialect() {
	    return new LayoutDialect();
	}
	
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		
		http.authorizeRequests()
			.antMatchers("/h2-console").permitAll()
			.antMatchers("/register", "/", "/about", "/login", "/forgotPassword", "/css/**", "/webjars/**").permitAll()
			.antMatchers("/profile").hasAnyRole("USER,ADMIN")
			.antMatchers("/users").hasRole("ADMIN")
			.and().formLogin().loginPage("/login").permitAll()
			.defaultSuccessUrl("/profile").and().logout().logoutSuccessUrl("/login");
		
		http.csrf().disable();
        http.headers().frameOptions().disable();
	}
}