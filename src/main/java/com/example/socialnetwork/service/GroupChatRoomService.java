package com.example.socialnetwork.service;

import com.example.socialnetwork.domain.ChatMessage;
import com.example.socialnetwork.domain.EMOJI;
import com.example.socialnetwork.domain.GroupChatRoom;
import com.example.socialnetwork.domain.User;
import com.example.socialnetwork.dto.*;
import com.example.socialnetwork.exception.IgnoredSocialNetworkException;
import com.example.socialnetwork.kafka_config.KafkaProducer;
import com.example.socialnetwork.repo.GroupChatRoomRepo;
import com.example.socialnetwork.security.oauth.SecurityCheck;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.errors.InvalidRequestException;
import org.apache.kafka.common.errors.ResourceNotFoundException;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class GroupChatRoomService {

    private final GroupChatRoomRepo groupChatRoomRepo;
    private final KafkaProducer kafkaProducer;
    private final UserService userService;
    private final ChatMessageService chatMessageService;
    private final SimpMessagingTemplate messagingTemplate;

    public GroupChatRoom findByGroupName(String groupName){
        return groupChatRoomRepo.findByGroupName(groupName)
                .orElseThrow(()-> new ResourceNotFoundException(
                        "Group with group name " + groupName + " not found")
                );
    }

    public void createGroup(CreateGroupDTO createGroupDTO){
        // is the user logged in ?
        if(SecurityCheck.isTheUserNotLoggedIn(createGroupDTO.ownerName())){
            throw new IgnoredSocialNetworkException("пользователь не зелогинен");
        }

        userService.findByEmail(createGroupDTO.ownerName());
        GroupChatRoom group = groupChatRoomRepo
                .findByGroupName(createGroupDTO.groupName())
                .orElse(null);

        if(group != null ) {
            throw new InvalidRequestException("Group name "
                    + createGroupDTO.groupName() + " already taken");
        }
        if(createGroupDTO.groupName() == null
                || createGroupDTO.groupName().isBlank()) {
            throw new InvalidRequestException("Group Name is required");
        }

        kafkaProducer.sendMessage(createGroupDTO);

    }

    public void addToGroup(AddToGroupDTO addToGroupDTO) {
        // is adder logged in?
        if(SecurityCheck.isTheUserNotLoggedIn(addToGroupDTO.addedBy())){
            throw new IgnoredSocialNetworkException("пользователь не залогинен");
        }

        GroupChatRoom groupChatRoom = findByGroupName(addToGroupDTO.groupName());
        userService.findByEmail(addToGroupDTO.username());
        User adderUser = userService.findByEmail(addToGroupDTO.addedBy()).orElseThrow();
        User userTobeAdded = userService.findByEmail(addToGroupDTO.username()).orElseThrow();

        List<User> userList = groupChatRoom.getUsers();
        if(!userList.contains(adderUser)){
            throw new InvalidRequestException(
                    "User " + adderUser.getEmail() + " not allowed to add users the group"
            );
        }

        if(userList.contains(userTobeAdded)) {
            throw new InvalidRequestException(
                    "User " + userTobeAdded.getEmail() + " is already in the group"
            );
        }
        kafkaProducer.sendMessage(addToGroupDTO);
    }

    public void sendMessage(ChatNotificationDTO chatNotification) {
        //is the sender the one logged in?
        if(SecurityCheck.isTheUserNotLoggedIn(chatNotification.getSender())){
            //delete attachment if it exists
            SecurityCheck.deleteAttachment(chatNotification);
            throw new IgnoredSocialNetworkException("пользователь не залогинен");
        }
        // check the type of the message
        if(!chatNotification.getType().equals("group")) {
            //delete attachment if it exists
            SecurityCheck.deleteAttachment(chatNotification);
            throw new InvalidRequestException(
                    "Message type is not allowed"
            );
        }

        //check if the attachment is attached
        var fileUrl = chatNotification.getFileUrl();
        if(fileUrl != null && !fileUrl.isBlank()) {
            if(!Files.exists(Paths.get(fileUrl))) {
                throw new InvalidRequestException(
                        "The required attachment was not sent successfully"
                );
            }
        }
        userService.findByEmail(chatNotification.getSender()); //check sender
        GroupChatRoom groupChatRoom = findByGroupName(chatNotification.getGroupName()); // check group

        //is the user the member of the group
        boolean isMember =  groupChatRoom.getUsers().stream()
                .anyMatch(u -> u.getEmail().equals(chatNotification.getSender()));

        if(!isMember){
            //delete attachment if it exists
            SecurityCheck.deleteAttachment(chatNotification);
            throw new IgnoredSocialNetworkException("пользователь не залогинен");
        }
        kafkaProducer.sendMessage(chatNotification);

    }

    public GroupChatRoom findGroupChatRoomById(Long id){
        return groupChatRoomRepo.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException(
                        "Group with group id " + id + " not found"
                ));
    }

    public ChatMessage saveMessage(ChatNotificationDTO chatNotification,
                                   GroupChatRoom groupChatRoom){
        User sender = userService.findByEmail(chatNotification.getSender()).orElseThrow();

        ChatMessage chatMessage = ChatMessage.builder()
                .groupId(groupChatRoom)
                .sender(sender)
                .text(chatNotification.getText())
                .fileName(chatNotification.getFileName())
                .fileUrl(chatNotification.getFileUrl())
                .type(chatNotification.getType())
                .timestamp(LocalDateTime.now())
                .build();

        return chatMessageService.saveGroupMessage(chatMessage);
    }

    public List<User> getGroupMembers(Long groupId){
        return groupChatRoomRepo.findUsersByGroupId(groupId);
    }

    public List<ChatNotificationDTO> getGroupMessages(String groupName) {
        GroupChatRoom groupChatRoom = findByGroupName(groupName);
        //IS the logged user a member of the group
        String loggedUsername = SecurityContextHolder.getContext()
                .getAuthentication().getName();
        boolean isMember = groupChatRoom.getUsers().stream()
                .anyMatch(u -> u.getEmail().equals(loggedUsername));

        if(!isMember){
            throw new IgnoredSocialNetworkException("пользователь не залогинен");
        }

        List<ChatNotificationDTO> chatNotificationList = new ArrayList<>();

        //find chat reactions
        chatMessageService
                .findChatMessagesByGroupName(groupName)
                .forEach(chatMessage -> {
                    //let get the chat reaction of each message
                    Map<String, EMOJI> rMap = new HashMap<>();
                    if(chatMessage.getChatReactionList() != null){
                        chatMessage.getChatReactionList()
                                .forEach(cr -> {
                                    rMap.put(cr.getUser().getEmail(),
                                            cr.getEmoji());
                                });
                    }
                    chatNotificationList.add(
                            ChatNotificationDTO.builder()
                                    .id(chatMessage.getId())
                                    .sender(chatMessage.getSender().getEmail())
                                    .groupName(chatMessage.getGroupId().getGroupName())
                                    .groupOwner(groupChatRoom.getOwner().getEmail())
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

    public void deleteGroupChat(String username, String groupName) {
        // is the owner name the one who logged in?
        if(SecurityCheck.isTheUserNotLoggedIn(username)){
            throw new IgnoredSocialNetworkException("пользователь не залогинен");
        }
        var groupChatRoom = findByGroupName(groupName);
        User owner = userService.findByEmail(username).orElseThrow();
        if(!groupChatRoom.getOwner().getId().equals(owner.getId())){
            throw new InvalidRequestException(
                    "The user " + username + " is not allowed to delete this group"
            );
        }

        //send event
        ChatNotificationDTO chatNotific = ChatNotificationDTO.builder()
                .groupName(groupName)
                .sender(username)
                .recipient("DELETE")
                .type("group")
                .text("DELETE " + groupName)
                .build();
        kafkaProducer.sendMessage(chatNotific);

    }
    // get group members
    public List<UserDTO> getMembersOfGroup(String groupName) {
        GroupChatRoom groupChatRoom = findByGroupName(groupName); //check if the group exists
        List<User> userList = groupChatRoomRepo
                .findUsersByGroupName(groupChatRoom.getGroupName());

        List<UserDTO> userDTOList = new ArrayList<>();
        userList.forEach(user -> {
            userDTOList.add(
                    UserDTO.builder()
                            .email(user.getEmail())
                            .phone(user.getPhone())
                            .name(user.getName())
                            .lastname(user.getLastname())
                            .build()
            );
        });
        return userDTOList;
    }

    public void updateGroupName(UpdateGroupNameDTO dto) {
        // is the logged user the owner
        if(SecurityCheck.isTheUserNotLoggedIn(dto.ownerName())){
            throw new IgnoredSocialNetworkException("пользователь не залогинен");
        }
        GroupChatRoom groupChatRoom = findByGroupName(dto.oldGroupName());
        User user = userService.findByEmail(dto.ownerName()).orElseThrow();
        if(!groupChatRoom.getOwner().getId().equals(user.getId())){
            throw new InvalidRequestException(
                    "The user " + dto.ownerName()
                            + " is not permitted to change group name"
            );
        }

        groupChatRoom.setGroupName(dto.newGroupName());
        groupChatRoomRepo.save(groupChatRoom);
    }

    public void deleteGroup(GroupChatRoom groupChatRoom){
        groupChatRoomRepo.delete(groupChatRoom);
    }

    public void saveGroup(GroupChatRoom groupChatRoom){
        groupChatRoomRepo.save(groupChatRoom);
    }
}
