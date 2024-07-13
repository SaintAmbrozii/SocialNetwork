package com.example.socialnetwork.controller;

import com.example.socialnetwork.dto.ChatReactionDTO;
import com.example.socialnetwork.payload.ApiResponse;
import com.example.socialnetwork.payload.ChatReactionReqResDTO;
import com.example.socialnetwork.service.ChatReactionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("api/chat-reactions")
public class ChatReactionController {

    private final ChatReactionService chatReactionService;

    public ChatReactionController(ChatReactionService chatReactionService) {
        this.chatReactionService = chatReactionService;
    }

    //get chat message reactions
    @GetMapping("/{chatMessageId}")
    public ResponseEntity<List<ChatReactionDTO>> getChatReactions(
            @PathVariable("chatMessageId") UUID chatMessageId){
        List<ChatReactionDTO> chatReactions
                = chatReactionService.getChatReactions(chatMessageId);
        return ResponseEntity.ok(chatReactions);
    }

    // add/remove chat message reactions
    @PostMapping("/add-remove")
    public ResponseEntity<ApiResponse> addChatResponse(
            @RequestBody ChatReactionReqResDTO chatReactionReqResDTO
    ){
        chatReactionService.addRemoveChatReaction(chatReactionReqResDTO);

        return ResponseEntity.ok(new ApiResponse(true, "success"));
    }
}
