package com.example.socialnetwork.service;

import com.example.socialnetwork.domain.*;
import com.example.socialnetwork.exception.IgnoredSocialNetworkException;
import com.example.socialnetwork.kafka_config.KafkaProducer;
import com.example.socialnetwork.dto.ChatNotificationDTO;
import com.example.socialnetwork.repo.ChatMessageRepo;
import com.example.socialnetwork.repo.GroupChatRoomRepo;
import com.example.socialnetwork.security.oauth.SecurityCheck;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.common.errors.InvalidRequestException;
import org.apache.kafka.common.errors.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
public class ChatMessageService {

    private final ChatMessageRepo chatMessageRepo;
    private final ChatRoomService chatRoomService;
    private final UserService userService;
    private final KafkaProducer kafkaProducer;
    private final GroupChatRoomRepo groupChatRoomRepo;

    public ChatMessage findChatMessageById(UUID chatMessageId){
        return chatMessageRepo.findByChatMessageId(chatMessageId)
                .orElseThrow(()-> new ResourceNotFoundException(
                        "Chat message with id " + chatMessageId + " not found")
                );
    }

    //send message
    public void sendMessage(ChatNotificationDTO chatNotification){
        //is the sender the logged one?
        if(SecurityCheck.isTheUserNotLoggedIn(chatNotification.getSender())){
            //deleteAttachment if exists
            SecurityCheck.deleteAttachment(chatNotification);
            throw new IgnoredSocialNetworkException("");
        }

        //check if the attachment is successfully send
        String filePath = chatNotification.getFileUrl();
        if(filePath != null  && !filePath.isBlank()){
            if(!Files.exists(Paths.get(filePath))) {
                throw new InvalidRequestException(
                        "The required attachment was not sent successfully"
                );
            }
        }

        userService.findByEmail(chatNotification.getSender());
        userService.findByEmail(chatNotification.getRecipient());

        kafkaProducer.sendMessage(chatNotification);
    }
    //save chat message
    public ChatMessage save(ChatNotificationDTO chatNotification) {

        User sender = userService.findByEmail(chatNotification.getSender()).orElseThrow();
        User recipient = userService.findByEmail(chatNotification.getRecipient()).orElseThrow();

        ChatMessage chatMessage =
                ChatMessage.builder()
                .sender(sender)
                .recipient(recipient)
                .text(chatNotification.getText())
                .fileName(chatNotification.getFileName())
                .fileUrl(chatNotification.getFileUrl())
                .type(chatNotification.getType())
                .timestamp(LocalDateTime.now())
                .build();

        var chatRoom = chatRoomService
                .getChatRoom(chatMessage.getSender().getId(),
                        chatMessage.getRecipient().getId(),
                        true
                )
                .orElseThrow(RuntimeException::new);


        chatMessage.setChatRoom(chatRoom);
        chatMessageRepo.save(chatMessage);
        return chatMessage;
    }

    public ChatMessage saveGroupMessage(ChatMessage chatMessage){
        return chatMessageRepo.save(chatMessage);
    }

    public List<ChatNotificationDTO> findChatMessages(String senderUsername, String recipientUsername) {

        //is the sender is the one who is logged in?
        if(SecurityCheck.isTheUserNotLoggedIn(senderUsername)){
            throw new IgnoredSocialNetworkException("не авторизирован");
        }

        User sender  = userService.findByEmail(senderUsername).orElseThrow();
        User recipient = userService.findByEmail(recipientUsername).orElseThrow();

        Long senderId = sender.getId();
        Long recipientId = recipient.getId();

        Optional<ChatRoom> chatRoom = chatRoomService.getChatRoom(
                senderId, recipientId, false);
        List<ChatMessage> chatMessages = chatRoom.map(cr ->
                        chatMessageRepo.findByChatId(cr.getChatId())
                )
                .orElse(List.of());

        List<ChatNotificationDTO> chatNotificationList = new ArrayList<>();

        chatMessages.forEach(chatMessage -> {
            //get message reactions
            Map<String, EMOJI> rMap = new HashMap<>();
            chatMessage.getChatReactionList()
                    .forEach(reaction ->
                            rMap.put(reaction.getUser().getName(),
                                    reaction.getEmoji())
                    );
            chatNotificationList.add(
                    ChatNotificationDTO.builder()
                            .id(chatMessage.getId())
                            .sender(chatMessage.getSender().getEmail())
                            .recipient(chatMessage.getRecipient().getEmail())
                            .text(chatMessage.getText())
                            .fileName(chatMessage.getFileName())
                            .fileUrl(chatMessage.getFileUrl())
                            .type(chatMessage.getType())
                            .reactions(rMap)
                            .build()
            );
        });

        return chatNotificationList;

    }

    public List<ChatMessage> findChatMessagesByGroupName(String groupName) {
        GroupChatRoom groupChatRoom = groupChatRoomRepo
                .findByGroupName(groupName)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Group with " + groupName + " not found"
                ));

        Long groupId = groupChatRoom.getId();

        return chatMessageRepo.findByGroupId(groupId);

    }

    //delete chat message by
    public void deleteChatMessage(ChatMessage chatMessage){
        chatMessageRepo.delete(chatMessage);
    }

}
