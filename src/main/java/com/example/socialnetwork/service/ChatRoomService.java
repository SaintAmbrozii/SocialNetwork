package com.example.socialnetwork.service;

import com.example.socialnetwork.domain.ChatRoom;
import com.example.socialnetwork.domain.User;
import com.example.socialnetwork.repo.ChatRoomRepo;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ChatRoomService {

    private final ChatRoomRepo chatRoomRepo;
    private final UserService userService;

    public Optional<ChatRoom> getChatRoom(
            Long sender,
            Long recipient,
            boolean createNewRoomIfNotExists
    ) {
        return chatRoomRepo
                .findBySenderAndRecipient(sender, recipient)
                .or(() -> {
                    if(createNewRoomIfNotExists) {
                        var chatRoom = createChatId(sender, recipient);
                        return Optional.of(chatRoom);
                    }

                    return  Optional.empty();
                });
    }

    @Transactional
    private ChatRoom createChatId(Long senderId, Long recipientId) {
        var chatId = String.format("%d_%d", senderId, recipientId);
        User sender = userService.findById(senderId);
        User recipient = userService.findById(recipientId);

        ChatRoom senderRecipient = ChatRoom
                .builder()
                .chatId(chatId)
                .sender(sender)
                .recipient(recipient)
                .build();

        ChatRoom recipientSender = ChatRoom
                .builder()
                .chatId(chatId)
                .sender(recipient)
                .recipient(sender)
                .build();

        chatRoomRepo.save(senderRecipient);
        chatRoomRepo.save(recipientSender);

        return senderRecipient;

    }
}
