package com.example.socialnetwork.repo;

import com.example.socialnetwork.domain.GroupChatRoom;
import com.example.socialnetwork.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface GroupChatRoomRepo extends JpaRepository<GroupChatRoom,Long> {

    @Query("SELECT gcr.users FROM GroupChatRoom gcr WHERE gcr.id=:groupId")
    List<User> findUsersByGroupId(Long groupId);

    @Query("SELECT gcr FROM GroupChatRoom gcr WHERE gcr.groupName=:groupName")
    Optional<GroupChatRoom> findByGroupName(String groupName);

    @Query("SELECT gcr.users FROM GroupChatRoom gcr WHERE gcr.groupName=:groupName")
    List<User> findUsersByGroupName(String groupName);
}
