package com.example.socialnetwork.security.oauth;


import com.example.socialnetwork.domain.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.Map;

public class UserPrincipal implements UserDetails, OAuth2User {

    private Long id;
    private String email;
    private String password;
    private String address;
    private String name;
    private Integer enabled;
    private Collection<? extends GrantedAuthority> authorities;
    private Map<String, Object> attributes;

    public UserPrincipal(Long id, String email, String password, String name, String address,
                         Integer enabled, Collection<? extends GrantedAuthority> authorities) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.name = name;
        this.address = address;
        this.enabled = enabled;
        this.authorities = authorities;
    }

    public static UserPrincipal create(User user) {
        Collection<GrantedAuthority> authorities = user.getAuthority();

        return new UserPrincipal(
                user.getId(),
                user.getName(),
                user.getPassword(),
                user.getLastname(),
                user.getAddress(),
                user.getEnabled(),
                authorities
        );
    }
    public static UserPrincipal create(User user, Map<String, Object> attributes) {
        UserPrincipal userPrincipal = UserPrincipal.create(user);
        userPrincipal.setAttributes(attributes);
        return userPrincipal;
    }

    @Override
    public Map<String, Object> getAttributes() {
        return attributes;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return enabled == 1;
    }

    @Override
    public String getName() {
        return  String.valueOf(id);
    }

    public Long getId() {
        return id;
    }

    public void setAttributes(Map<String, Object> attributes) {
        this.attributes = attributes;
    }

    public String getFullAddress() {
        return address;
    }

    public String getPersonFullName() {
        return name;
    }


}
