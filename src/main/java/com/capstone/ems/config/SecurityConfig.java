package com.capstone.ems.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.capstone.ems.auth.JwtAuthFilter;
import com.capstone.ems.enums.UserType;
import com.capstone.ems.service.UserEntityDetailService;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
	
	@Autowired
	private UserEntityDetailService userEntityDetailService;

	@Autowired
    private JwtAuthFilter jwtAuthFilter;
	
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
    System.out.println("SECURITY CONFIG: Entered the Security Filter Chain");
        httpSecurity.csrf(AbstractHttpConfigurer::disable)
        		.cors(Customizer.withDefaults())
        		.authorizeHttpRequests(registry -> {
                    registry.requestMatchers("/home", "/register/**", "/auth/**", "/all/**").permitAll();
                    registry.requestMatchers("/admin/**").hasAuthority("ADMIN");
                    registry.requestMatchers("/user/**").hasAuthority("EMPLOYEE");
                    registry.requestMatchers("/manager/**").hasAuthority("MANAGER");
                    registry.requestMatchers("/adminuser/**").hasAnyAuthority("ADMIN","EMPLOYEE");
                    registry.requestMatchers("/adminmanager/**").hasAnyAuthority("ADMIN","MANAGER");
                    registry.requestMatchers("/manageruser/**").hasAnyAuthority("EMPLOYEE","MANAGER");
                    registry.anyRequest().authenticated();
                })
                .sessionManagement(manager->manager.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authenticationProvider()).addFilterBefore(
                        jwtAuthFilter, UsernamePasswordAuthenticationFilter.class
                );
        System.out.println("SECURITY CONFIG: returning from security filter chain");
        return httpSecurity.build();
    }
    
    @Bean
    public AuthenticationProvider authenticationProvider() {
    	System.out.println("SECURITY CONFIG: Entered authentication provider");
    	DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
    	provider.setUserDetailsService(userEntityDetailService);
    	provider.setPasswordEncoder(passwordEncoder());
    	return provider;
    }
    
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
    	System.out.println("SECURITY CONFIG: Entered authentication manager");
		return authenticationConfiguration.getAuthenticationManager();
    	
    }

}
