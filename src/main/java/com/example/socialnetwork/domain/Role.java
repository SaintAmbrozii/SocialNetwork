package com.example.socialnetwork.domain;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;

@RequiredArgsConstructor
public enum Role implements GrantedAuthority {

 USER("USER"),ADMIN("ADNIN");


   private final String roles;
    @Override
    public String getAuthority() {
        return roles;
    }
}
