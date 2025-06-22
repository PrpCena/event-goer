package com.prp.tickets.config;


import com.prp.tickets.filters.UserProvisioningFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.server.resource.web.authentication.BearerTokenAuthenticationFilter;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {
  
  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http, UserProvisioningFilter userProvisioningFilter) throws
																										   Exception {
	http
	  .authorizeHttpRequests(authorize ->
							   // All requests must be authenticated
							   authorize
								 .anyRequest()
								 .permitAll()) // change this to authenticated
	  .csrf(csrf -> csrf.disable())
	  .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
	  .oauth2ResourceServer(oauth2 -> oauth2.jwt(Customizer.withDefaults()))
	  .addFilterAfter(userProvisioningFilter, BearerTokenAuthenticationFilter.class);
	return http.build();
  }
  
}
