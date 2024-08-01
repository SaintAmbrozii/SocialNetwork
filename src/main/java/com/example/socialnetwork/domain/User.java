package com.example.socialnetwork.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.Hibernate;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;


@Entity
@Table(name = "users",schema = "public")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@ToString(callSuper = true)
public class User  {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id",nullable = false)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "lastname")
    private String lastname;

    @Column(name = "email",unique = true)
    private String email;

    @Column(name = "phone",unique = true)
    private String phone;

    @Column(name = "password")
    private String password;

    @Column(name = "gender")
    private String gender;

    @Column(name = "locale")
    private String locale;

    @Column(name = "address")
    private String address;

    @Column(name = "auth_provider")
    @Enumerated(EnumType.STRING)
    private AuthProvider provider;

    @Column(name = "provider_id")
    private String providerId;

    @Column(name = "picture")
    private String picture;

    @Column(name = "enabled")
    private Integer enabled;


    @ElementCollection(targetClass = Role.class, fetch = FetchType.EAGER)
    @CollectionTable(name = "authorities", joinColumns = @JoinColumn(name = "user_id"))
    @Enumerated(EnumType.STRING)
    private Set<GrantedAuthority> authority = new HashSet<>();




    @OneToOne
    private Account account;

    @OneToMany(mappedBy = "user")
    private List<Token> tokenList;



    public User(long l, String test, String s, String s1) {
    }







}
