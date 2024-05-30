package com.capstone.ems.domain.entities;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Collection;

import org.junit.jupiter.api.Test;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import com.capstone.ems.enums.UserType;

class UserEntityTests {

    @Test
    void testGetAuthorities() {
        UserEntity userEntity = new UserEntity();
        userEntity.setRole(UserType.ADMIN);

        Collection<? extends GrantedAuthority> authorities = userEntity.getAuthorities();

        assertEquals(1, authorities.size());
        assertTrue(authorities.contains(new SimpleGrantedAuthority("ADMIN")));
    }

    @Test
    void testGetUsername() {
        UserEntity userEntity = new UserEntity();
        userEntity.setEmail("john.doe@example.com");

        assertEquals("john.doe@example.com", userEntity.getUsername());
    }

    @Test
    void testIsAccountNonExpired() {
        UserEntity userEntity = new UserEntity();

        assertTrue(userEntity.isAccountNonExpired());
    }

    @Test
    void testIsAccountNonLocked() {
        UserEntity userEntity = new UserEntity();

        assertTrue(userEntity.isAccountNonLocked());
    }

    @Test
    void testIsCredentialsNonExpired() {
        UserEntity userEntity = new UserEntity();

        assertTrue(userEntity.isCredentialsNonExpired());
    }

    @Test
    void testIsEnabled() {
        UserEntity userEntity = new UserEntity();

        assertTrue(userEntity.isEnabled());
    }
}
