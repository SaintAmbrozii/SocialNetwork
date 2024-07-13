package com.example.socialnetwork.service;

import com.example.socialnetwork.domain.ChatMessage;
import com.example.socialnetwork.domain.ChatReaction;
import com.example.socialnetwork.dto.ChatReactionDTO;
import com.example.socialnetwork.exception.IgnoredSocialNetworkException;
import com.example.socialnetwork.kafka_config.KafkaProducer;
import com.example.socialnetwork.payload.ChatReactionReqResDTO;
import com.example.socialnetwork.repo.ChatReactionRepo;
import com.example.socialnetwork.security.oauth.SecurityCheck;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ChatReactionService {

    private final ChatMessageService chatMessageService;
    private final ChatReactionRepo chatReactionRepo;
    private final UserService userService;
    private final KafkaProducer kafkaProducer;

    public List<ChatReactionDTO> getChatReactions(UUID chatMessageId) {
        ChatMessage chatMessage = chatMessageService
                .findChatMessageById(chatMessageId);

        //is the user allowed to get the reactions
        String loggedUsername = SecurityContextHolder.getContext()
                .getAuthentication().getName();

        if(chatMessage.getGroupId() == null) { //private chat
            if(!chatMessage.getSender().getEmail().equals(loggedUsername)
                    && !chatMessage.getRecipient().getEmail().equals(loggedUsername)
            ) {
                throw new IgnoredSocialNetworkException("пользователь не залогинен");
            }
        } else { //group message
            boolean isMember
                    =  chatMessage.getGroupId().getUsers()
                    .stream()
                    .anyMatch(u -> u.getEmail().equals(loggedUsername));

            if(!isMember){
                throw new IgnoredSocialNetworkException("пользователь не залогинен");
            }
        }

        return chatReactionRepo.findByChatMessage(chatMessageId)
                .stream()
                .map(cr ->
                        ChatReactionDTO.builder()
                                .chatId(cr.getChatMessage().getId())
                                .emoji(cr.getEmoji().name())
                                .username(cr.getUser().getEmail())
                                .createdAt(cr.getCreatedAt())
                                .build()
                )
                .toList();
    }

    // add - remove chat reactions
    public void addRemoveChatReaction(
            ChatReactionReqResDTO dto
    ) {
        //is the user the one logged in ?
        if(SecurityCheck.isTheUserNotLoggedIn(dto.getUsername())){
            throw new IgnoredSocialNetworkException("пользователь не залогинен");
        }
        // check if the chat message id and username are valid
        chatMessageService.findChatMessageById(dto.getChatMessageId());
        userService.findByEmail(dto.getUsername());

        //find chat message reaction by chat message id and username
        findByChatMessageAndUser(dto.getChatMessageId(), dto.getUsername())
                .ifPresentOrElse(chatReaction -> {
                            if(chatReaction.getEmoji().toString().equals(dto.getEmoji())){
                                // status delete
                                dto.setStatus("DELETE");
                            } else {
                                // status update
                                dto.setStatus("UPDATE");
                            }
                        } ,() -> { // chat message reaction not found
                            //check if user is allowed to send reaction to the message
                            ChatMessage chatMessage = chatMessageService
                                    .findChatMessageById(dto.getChatMessageId());
                            if(chatMessage.getGroupId() == null) { //private chat
                                if(!chatMessage.getSender().getEmail().equals(dto.getUsername())
                                        && !chatMessage.getRecipient().getEmail().equals(dto.getUsername())){
                                    throw new IgnoredSocialNetworkException("");
                                }
                            } else { //group message
                                boolean isMember =  chatMessage.getGroupId().getUsers()
                                        .stream()
                                        .anyMatch(u -> u.getEmail().equals(dto.getUsername()));

                                if(!isMember){
                                    throw new IgnoredSocialNetworkException("пользователь не залогинен");
                                }
                            }
                            // status new
                            dto.setStatus("ADD");
                        }
                );

        kafkaProducer.sendMessage(dto);
    }

    //find by Message and User
    public Optional<ChatReaction> findByChatMessageAndUser(UUID chatMessageId, String username){
        return chatReactionRepo.findByChatMessageAndUser(
                chatMessageId,  username);
    }

    //save
    public void save(ChatReaction chatReaction){
        chatReactionRepo.save(chatReaction);
    }

    //delete
    public void delete(ChatReaction chatReaction){
        chatReactionRepo.delete(chatReaction);
    }
}
