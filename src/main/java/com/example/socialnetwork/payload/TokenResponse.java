package com.example.socialnetwork.payload;

import lombok.*;

@Builder
@Getter
@Setter
public class TokenResponse {

    private String token;
    private String refreshToken;
    private final String type = "Bearer";
}
