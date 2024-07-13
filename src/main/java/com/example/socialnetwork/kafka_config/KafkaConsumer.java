package com.example.socialnetwork.kafka_config;

import com.example.socialnetwork.domain.*;
import com.example.socialnetwork.dto.AddToGroupDTO;
import com.example.socialnetwork.dto.ChatNotificationDTO;
import com.example.socialnetwork.dto.CreateGroupDTO;
import com.example.socialnetwork.payload.ChatReactionReqResDTO;
import com.example.socialnetwork.service.ChatMessageService;
import com.example.socialnetwork.service.ChatReactionService;
import com.example.socialnetwork.service.GroupChatRoomService;
import com.example.socialnetwork.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class KafkaConsumer {

    private final SimpMessagingTemplate messagingTemplate;
    private final ChatMessageService chatMessageService;
    private final GroupChatRoomService groupChatRoomService;
    private final ChatReactionService chatReactionService;
    private final UserService userService;

    @KafkaListener(topics = "chat-topic", groupId = "myGroup")
    public void receiveMessage(@Payload ConsumerRecord<String, Object> record,
                               Acknowledgment acknowledgment
    ) {
        Object obj = record.value();
//        if(messageType.equals("chatNotification")) {
        //chatNotification for private and group (also group delete) chat
        if(obj instanceof ChatNotificationDTO){
            processPrivateAndGroupMessages((ChatNotificationDTO) obj);
            acknowledgment.acknowledge();  // manual acknowledgment

        } else if(obj instanceof ChatReactionReqResDTO){
            //chatReaction process
            processChatReaction((ChatReactionReqResDTO) obj);
            acknowledgment.acknowledge(); // manual acknowledgment
        } else if(obj instanceof CreateGroupDTO) {
            //store event of create group
            processCreateGroupEvent((CreateGroupDTO) obj);
            acknowledgment.acknowledge(); // manual acknowledgment
        } else if(obj instanceof AddToGroupDTO) {
            //event of add to a group
            processAddToGroupEvent((AddToGroupDTO) obj);
            acknowledgment.acknowledge(); // manual
        }
        else {
            System.out.println("unknown  type " + obj.getClass());
        }
    }

    private void processAddToGroupEvent(AddToGroupDTO dto) {
        GroupChatRoom groupChatRoom
                = groupChatRoomService.findByGroupName(dto.groupName());
        User userTobeAdded = userService.findByEmail(dto.username()).orElseThrow();

        List<User> userList = groupChatRoom.getUsers();
        userList.add(userTobeAdded);
        groupChatRoom.setUsers(userList);
        groupChatRoomService.saveGroup(groupChatRoom);
        //create chat notification
        String textMsg = String.format("%s add %s to the group",
                dto.addedBy(), dto.username());
        ChatNotificationDTO chatNotification =
                ChatNotificationDTO.builder()
                        .sender(dto.addedBy())
                        .groupName(groupChatRoom.getGroupName())
                        .type("USER-ADDED")
                        .text(textMsg)
                        .build();
        //save message
        groupChatRoomService.saveMessage(chatNotification,groupChatRoom);
        //send notification to group members
        userList.forEach(user -> {
            messagingTemplate.convertAndSendToUser(
                    user.getEmail(),
                    "/my-groups",
                    chatNotification
            );
        });
    }

    private void processCreateGroupEvent(CreateGroupDTO obj) {
        User user = userService.findByEmail(obj.ownerName()).orElseThrow();
        GroupChatRoom groupChatRoom = GroupChatRoom.builder()
                .owner(user)
                .groupName(obj.groupName())
                .users(List.of(user))
                .build();

        groupChatRoomService.saveGroup(groupChatRoom);
    }

    //process chat reaction
    public void processChatReaction(ChatReactionReqResDTO dto){

        chatReactionService
                .findByChatMessageAndUser(dto.getChatMessageId(), dto.getUsername())
                .ifPresentOrElse((chatReaction) ->{
                    if(chatReaction.getEmoji().toString().equals(dto.getEmoji())) {
                        chatReactionService.delete(chatReaction);
                    } else {
                        chatReaction.setEmoji(EMOJI.valueOf(dto.getEmoji()));
                        chatReaction.setCreatedAt(LocalDateTime.now());
                        chatReactionService.save(chatReaction);
                    }
                }, () -> {
                    //create one
                    User user = userService.findByEmail(dto.getUsername()).orElseThrow();
                    ChatMessage chatMessage = chatMessageService
                            .findChatMessageById(dto.getChatMessageId());

                    chatReactionService.save(
                            ChatReaction.builder()
                                    .chatMessage(chatMessage)
                                    .user(user)
                                    .emoji(EMOJI.valueOf(dto.getEmoji()))
                                    .createdAt(LocalDateTime.now())
                                    .build()
                    );
                });

        //send notification.
        //identify if it is private or group message
        ChatMessage chatMessage =
                chatMessageService.findChatMessageById(dto.getChatMessageId());


        if(chatMessage.getGroupId() == null) {
            //it is private message so, send notification to both sender and receiver
            //identify the recipient name of the chat reaction
            String recipientName = "";
            if(chatMessage.getSender().getEmail().equals(dto.getUsername())){
                recipientName = chatMessage.getRecipient().getEmail();
            } else if(chatMessage.getRecipient().getEmail().equals(dto.getUsername())){
                recipientName = chatMessage.getSender().getEmail();
            }

            //send message notification to receiver
            messagingTemplate.convertAndSendToUser(
                    recipientName,
                    "/message-reaction",
                    ChatNotificationDTO.builder()
                            .sender(dto.getUsername())
                            .recipient(recipientName)
                            .build()
            );
            //send message notification to the sender
            messagingTemplate.convertAndSendToUser(
                    dto.getUsername(),
                    "/message-reaction",
                    ChatNotificationDTO.builder()
                            .sender(dto.getUsername())
                            .recipient(recipientName)
                            .build()
            );

        } else {
            // it is group message
            GroupChatRoom groupChatRoom = groupChatRoomService
                    .findGroupChatRoomById(chatMessage.getGroupId().getId());

            // send a web socket notification for each group members
            groupChatRoomService
                    .getGroupMembers(groupChatRoom.getId())
                    .forEach(user ->{
                        messagingTemplate.convertAndSendToUser(
                                user.getEmail(),
                                "/message-reaction",
                                ChatNotificationDTO.builder()
                                        .sender(dto.getUsername())
                                        .groupName(groupChatRoom.getGroupName())
                                        .build()
                        );
                    });
        }
    }
    // process private and group chat and send web socket notifications
    public void processPrivateAndGroupMessages(ChatNotificationDTO chatNotification){
        if(chatNotification.getType().equals("private")){
            //save message
            ChatMessage savedMsg = chatMessageService.save(chatNotification);

//            // add reactions for the message
            Map<String, EMOJI> rMap = new HashMap<>();
//            if(savedMsg.getChatReactionList() != null){
//                savedMsg.getChatReactionList()
//                .forEach(reaction ->
//                        rMap.put(reaction.getUser().getUsername(),
//                                reaction.getEmoji())
//                );
//            }

            //send message notification to receiver
            messagingTemplate.convertAndSendToUser(
                    chatNotification.getRecipient(),
                    "/my-messages",
                    ChatNotificationDTO.builder()
                            .id(savedMsg.getId())
                            .sender(savedMsg.getSender().getEmail())
                            .recipient(savedMsg.getRecipient().getEmail())
                            .text(savedMsg.getText())
                            .fileName(savedMsg.getFileName())
                            .fileUrl(savedMsg.getFileUrl())
                            .reactions(rMap)
                            .build()
            );
            //send notification to the sender
            messagingTemplate.convertAndSendToUser(
                    chatNotification.getSender(),
                    "/my-messages",
                    ChatNotificationDTO.builder()
                            .id(savedMsg.getId())
                            .sender(savedMsg.getSender().getEmail())
                            .recipient(savedMsg.getRecipient().getEmail())
                            .text(savedMsg.getText())
                            .fileName(savedMsg.getFileName())
                            .fileUrl(savedMsg.getFileUrl())
                            .reactions(rMap)
                            .build()
            );
        }
        // group notification
        else if(chatNotification.getType().equals("group")){
            GroupChatRoom groupChatRoom =
                    groupChatRoomService.findByGroupName(chatNotification.getGroupName());

            if(chatNotification.getRecipient() != null && chatNotification.getRecipient().equals("DELETE")){
                // it is a delete a group request
                List<ChatMessage> chatMessageLists = chatMessageService
                        .findChatMessagesByGroupName(groupChatRoom.getGroupName());
                //delete message reactions for each message
                chatMessageLists.forEach(chatMessage -> {
                    if(chatMessage.getChatReactionList() != null){
                        chatMessage.getChatReactionList().forEach(chatReactionService::delete);
                    }
                });
                //delete group messages
                chatMessageLists.forEach(chatMessageService::deleteChatMessage);

                //send notification
                groupChatRoomService.getGroupMembers(groupChatRoom.getId())
                        .forEach(user -> {
                            //send message notification to group members
                            messagingTemplate.convertAndSendToUser(
                                    user.getEmail(),
                                    "/my-groups",
                                    ChatNotificationDTO.builder()
                                            .id(null)
                                            .groupName(chatNotification.getGroupName())
                                            .sender(chatNotification.getSender())
                                            .recipient("DELETE")
                                            .text(null)
                                            .fileName(null)
                                            .fileUrl(null)
                                            .build()
                            );
                        });
                // delete group chat room
                groupChatRoomService.deleteGroup(groupChatRoom);
            } else {    // it is a group message
                //save message
                ChatMessage savedMsg =  groupChatRoomService
                        .saveMessage(chatNotification, groupChatRoom);

                //add message reaction
                Map<String, EMOJI> rMap = new HashMap<>();
//                if(savedMsg.getChatReactionList() != null){
//                    savedMsg.getChatReactionList()
//                            .forEach(cr ->{
//                                rMap.put(cr.getUser().getUsername(),
//                                        cr.getEmoji());
//                            });
//                }

                groupChatRoomService.getGroupMembers(groupChatRoom.getId())
                        .forEach(user -> {
                            //send message notification to group members
                            messagingTemplate.convertAndSendToUser(
                                    user.getEmail(),
                                    "/my-groups",
                                    ChatNotificationDTO.builder()
                                            .id(savedMsg.getId())
                                            .sender(savedMsg.getSender().getEmail())
                                            .groupName(savedMsg.getGroupId().getGroupName())
                                            .text(savedMsg.getText())
                                            .fileName(savedMsg.getFileName())
                                            .fileUrl(savedMsg.getFileUrl())
                                            .reactions(rMap)
                                            .build()
                            );
                        });
            }

        }
    }
}
