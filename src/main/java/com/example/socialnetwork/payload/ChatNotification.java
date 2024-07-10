package com.example.socialnetwork.payload;

import com.example.socialnetwork.domain.EMOJI;
import lombok.*;

import java.util.Map;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class ChatNotification {

   private UUID id;
   private String sender;
   private String recipient;
   private String groupName;
   private String groupOwner;
   private String text;
   private String fileName;
   private String fileUrl;
   private String type;
   private Map<String, EMOJI> reactions;
}
