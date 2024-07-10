package com.example.socialnetwork.security.oauth;

import com.example.socialnetwork.payload.ChatNotification;
import org.springframework.security.core.context.SecurityContextHolder;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class SecurityCheck {

    public static boolean isTheUserNotLoggedIn(String username){
        return   !SecurityContextHolder.getContext().getAuthentication().getName()
                .equals(username);
    }

    public static void deleteAttachment(ChatNotification chatNotification) {
        String fileUrl = chatNotification.getFileUrl();
        if(fileUrl != null && !fileUrl.isBlank()){
            Path filePath = Paths.get(fileUrl);
            if(Files.exists(filePath)) {
                try {
                    Files.delete(filePath);
                } catch (IOException e) {
                    System.out.println(e.getMessage());
                }
            }
        }
    }
}
