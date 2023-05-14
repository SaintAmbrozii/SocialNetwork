package com.example.socialnetwork.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class NotFoundSocialNetworkException extends SocialNetworkException{

    public NotFoundSocialNetworkException(String message) {
        super(message);
    }
}
