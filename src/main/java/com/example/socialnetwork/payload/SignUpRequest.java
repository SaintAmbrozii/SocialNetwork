package com.example.socialnetwork.payload;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SignUpRequest {

    private String email;

    private String password;

    private String name;

    private String lastname;

    private String phone;
}
