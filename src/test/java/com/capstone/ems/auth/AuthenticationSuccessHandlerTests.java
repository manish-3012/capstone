package com.capstone.ems.auth;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.Collections;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class AuthenticationSuccessHandlerTests {

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private Authentication authentication;

    private RedirectStrategy redirectStrategy;

    private AuthenticationSuccessHandler successHandler;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        redirectStrategy = new DefaultRedirectStrategy();
        successHandler = new AuthenticationSuccessHandler();
        successHandler.setRedirectStrategy(redirectStrategy);
    }

//    @Test
//    public void testOnAuthenticationSuccess_Admin() throws ServletException, IOException {
//        // Arrange
//        GrantedAuthority adminAuthority = new SimpleGrantedAuthority("ROLE_ADMIN");
//        Set<GrantedAuthority> authorities = new HashSet<>();
//        authorities.add(adminAuthority);
//        when(authentication.getAuthorities()).thenReturn(authorities);
//
//        // Act
//        successHandler.onAuthenticationSuccess(request, response, authentication);
//
//        // Assert
//        ArgumentCaptor<String> urlCaptor = ArgumentCaptor.forClass(String.class);
//        verify(response).sendRedirect(urlCaptor.capture());
//        assertEquals("/admin/home", urlCaptor.getValue());
//    }
//
//    @Test
//    public void testOnAuthenticationSuccess_User() throws ServletException, IOException {
//        // Arrange
//        GrantedAuthority authority = () -> "ROLE_USER";
//        Collection<GrantedAuthority> authorities = Collections.singletonList(authority);
//        when(authentication.getAuthorities()).thenReturn(authorities);
//
//        // Act
//        successHandler.onAuthenticationSuccess(request, response, authentication);
//
//        // Assert
//        ArgumentCaptor<String> urlCaptor = ArgumentCaptor.forClass(String.class);
//        verify(response).sendRedirect(urlCaptor.capture());
//        assertEquals("/user/home", urlCaptor.getValue());
//    }
    
    @Test
    public void testOnAuthenticationSuccess_NoRoles() throws ServletException, IOException {
        when(authentication.getAuthorities()).thenReturn(Collections.emptySet());

        successHandler.onAuthenticationSuccess(request, response, authentication);

        verify(response, never()).sendRedirect(anyString());
    }
}