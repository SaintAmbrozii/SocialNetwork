package com.example.socialnetwork.dto;

import lombok.Builder;

@Builder
public record CreateGroupResponse(Long groupId, String groupName, String ownerName) {
}
