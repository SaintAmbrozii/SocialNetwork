package com.example.socialnetwork.mapper;

import com.example.socialnetwork.domain.UserContact;
import com.example.socialnetwork.domain.searchcriteria.AccountSearchCriteria;
import com.example.socialnetwork.domain.searchcriteria.ContactsSearchCriteria;
import com.example.socialnetwork.dto.AccountDTO;
import com.example.socialnetwork.dto.UserContactDTO;
import com.example.socialnetwork.dto.UserDTO;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserContactMapper extends EntityMapper<UserContactDTO,UserContact> {


    List<UserContactDTO> convertToDtoList(List<UserContact> contactList);

    AccountSearchCriteria contactSearchCriteriaToAccountSearch(ContactsSearchCriteria criteria);

    UserContactDTO accountDtoToFriendDto(AccountDTO accountDto);
}
