package com.example.socialnetwork.dto;

import com.example.socialnetwork.domain.EMOJI;
import lombok.*;

import java.util.Map;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ChatNotificationDTO {

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
