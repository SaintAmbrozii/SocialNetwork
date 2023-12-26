package com.example.socialnetwork.payload;

import com.example.socialnetwork.domain.Role;
import com.example.socialnetwork.domain.User;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;

import java.util.Set;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDTO {

    private Long id;
    private String name;
    private String lastname;
    private String email;
    private String password;
    private String phone;
    private String picture;
    private Integer enabled;
    private String address;
    private String locale;
    private String gender;

    private Set<GrantedAuthority> authority;


}
