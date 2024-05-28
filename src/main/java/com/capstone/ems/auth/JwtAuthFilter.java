package com.capstone.ems.auth;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.capstone.ems.service.UserEntityDetailService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private UserEntityDetailService userEntityDetailService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

//    	System.out.println("HTTP Request: " + request);
    	final String authHeader = request.getHeader("Authorization");
        final String jwtToken;
        final String userEmail;

//        System.out.println("Entered doFilterInternal with jwtToken: " + authHeader);
        if (authHeader == null) {
//        	System.out.println("Auth Header is Blank returning");
            filterChain.doFilter(request, response);
            return;
        }

        jwtToken = authHeader.substring(7);
        userEmail = jwtUtils.extractUsername(jwtToken);

        if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = userEntityDetailService.loadUserByUsername(userEmail);
            
//            System.out.println("User Details fetched inside doFilterInternal as: " + userDetails);

            if (jwtUtils.isTokenValid(jwtToken, userDetails)) {
//            	System.out.println("User Details: " + userDetails);
                SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
//                System.out.println("Security Context: " + securityContext);
                UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities()
                );
//                System.out.println("UsernamePasswordAuthenticationToken: " + token);
                token.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                securityContext.setAuthentication(token);
                
//                System.out.println("Security Context: " + securityContext);
                
                SecurityContextHolder.setContext(securityContext);
            }
        }
        
//        System.out.println("JwtAuthFilter is passing to another filter in the chain.");
        filterChain.doFilter(request, response);
    }
}
