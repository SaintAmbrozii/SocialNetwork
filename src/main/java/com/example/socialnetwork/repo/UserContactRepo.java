package com.example.socialnetwork.repo;


import com.example.socialnetwork.domain.UserContact;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;



public interface UserContactRepo extends JpaRepository<UserContact, Long> , JpaSpecificationExecutor<UserContact> {



}
