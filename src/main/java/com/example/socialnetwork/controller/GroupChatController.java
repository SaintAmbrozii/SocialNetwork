package com.example.socialnetwork.controller;

import com.example.socialnetwork.dto.*;
import com.example.socialnetwork.payload.ApiResponse;
import com.example.socialnetwork.service.GroupChatRoomService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/group-chat")
public class GroupChatController {

    private final GroupChatRoomService groupChatRoomService;

    public GroupChatController(GroupChatRoomService groupChatRoomService) {
        this.groupChatRoomService = groupChatRoomService;
    }

    // send message to a group
    @PostMapping("/send")
    public ResponseEntity<ApiResponse> sendMessage(
            @RequestBody ChatNotificationDTO chatNotification){

        groupChatRoomService.sendMessage(chatNotification);
        return ResponseEntity.ok(new ApiResponse(
                true, "Message Sent Successfully")
        );
    }
    //get group chat messages
    @GetMapping("/messages/{groupName}")
    public ResponseEntity<List<ChatNotificationDTO>> findGroupMessages(
            @PathVariable("groupName") String groupName
    ){
        List<ChatNotificationDTO> chatNotificationList
                = groupChatRoomService.getGroupMessages(groupName);

        return ResponseEntity.ok(chatNotificationList);
    }
    // create group
    @PostMapping("/create-group")
    public ResponseEntity<ApiResponse> createGroup(
            @RequestBody CreateGroupDTO createGroupDto
    ){
        groupChatRoomService.createGroup(createGroupDto);

        return ResponseEntity.ok(new ApiResponse(true,"group successfully created"));
    }

    // add a user to group
    @PostMapping("add-to-group")
    public ResponseEntity<ApiResponse> addUserToGroupChat(
            @RequestBody AddToGroupDTO addToGroupDTO)
    {
        groupChatRoomService.addToGroup(addToGroupDTO);
        return ResponseEntity.ok(new ApiResponse(
                true, " added to success")
        );
    }

    //update groups name
    @PutMapping("change-name")
    public ResponseEntity<ApiResponse> changeGroupName(
            @RequestBody UpdateGroupNameDTO dto
    ){
        groupChatRoomService.updateGroupName(dto);
        return ResponseEntity.ok(new ApiResponse(
                true, " change group name to success")
        );
    }

    // get users of a group
    @GetMapping("members/{groupName}")
    public ResponseEntity<List<UserDTO>> getMembersOfGroup(
            @PathVariable("groupName") String groupName
    ){
        List<UserDTO> membersOfGroup
                = groupChatRoomService.getMembersOfGroup(groupName);
        return ResponseEntity.ok(membersOfGroup);
    }

    //delete a group
    @DeleteMapping("delete-group/{groupName}/{ownerName}")
    public ResponseEntity<ApiResponse> deleteGroup(
            @PathVariable("groupName") String groupName,
            @PathVariable("ownerName") String ownerName){
        groupChatRoomService.deleteGroupChat(ownerName, groupName);

        return ResponseEntity.ok(
                new ApiResponse(
                        true,   "Success"
                )
        );

    }

}
