package com.example.socialnetwork.payload;

import lombok.*;

@Builder
@Getter
@Setter
public class TokenResponse {

    private final String type = "Bearer";
    private String token;
    private String refreshToken;
}
