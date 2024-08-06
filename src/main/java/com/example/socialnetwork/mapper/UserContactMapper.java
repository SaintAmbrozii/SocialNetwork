package com.example.socialnetwork.mapper;

import com.example.socialnetwork.domain.UserContact;
import com.example.socialnetwork.dto.AccountDTO;
import com.example.socialnetwork.dto.UserContactDTO;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserContactMapper {

    @Mapping(target = "id", source = "id")
    UserContactDTO convertToDto(UserContact contact);

    @Mapping(target = "fromAccountId", source = "id")
    UserContactDTO userDtoToContactDto(UserContactDTO userDto);

    @InheritInverseConfiguration
    UserContact convertToEntity(UserContactDTO userContactDTO);

    List<UserContactDTO> convertToDtoList(List<UserContact> contactList);

    UserContactDTO accountDtoToFriendDto(AccountDTO accountDto);
}
