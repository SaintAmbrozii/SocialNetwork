package com.example.socialnetwork.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.CONTINUE)
public class IgnoredSocialNetworkException extends SocialNetworkException{

    public IgnoredSocialNetworkException(String message) {
        super(message);
    }
}
