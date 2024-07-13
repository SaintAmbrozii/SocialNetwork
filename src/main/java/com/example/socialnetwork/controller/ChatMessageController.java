package com.example.socialnetwork.controller;

import com.example.socialnetwork.dto.ChatNotificationDTO;
import com.example.socialnetwork.payload.ApiResponse;
import com.example.socialnetwork.service.ChatMessageService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/chat")
public class ChatMessageController {

    private final ChatMessageService chatMessageService;

    public ChatMessageController(ChatMessageService chatMessageService) {
        this.chatMessageService = chatMessageService;
    }

    @PostMapping("/send")
    public ResponseEntity<ApiResponse> sendMessage(
            @RequestBody ChatNotificationDTO chatNotification
    )
    {

        chatMessageService.sendMessage(chatNotification);
        return ResponseEntity.ok(new ApiResponse(
                true,"message send successfully"));
    }

    @GetMapping("/messages/{sender}/{recipient}")
    public ResponseEntity<List<ChatNotificationDTO>> findChatMessages(
            @PathVariable String sender,
            @PathVariable String recipient)
    {
        List<ChatNotificationDTO> chatMessagesList
                = chatMessageService.findChatMessages(sender, recipient);
        return ResponseEntity.ok(chatMessagesList);
    }

}
