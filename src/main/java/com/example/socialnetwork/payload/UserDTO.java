package com.example.socialnetwork.payload;

import com.example.socialnetwork.domain.Role;
import com.example.socialnetwork.domain.User;
import jakarta.persistence.*;
import lombok.*;

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
    private String avatarUri;
    private boolean isActive;

    private Set<Role> roles;


}
