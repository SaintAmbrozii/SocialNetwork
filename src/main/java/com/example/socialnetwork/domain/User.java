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


    @ManyToMany(cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    @JoinTable(name = "users_likes",
            joinColumns = @JoinColumn(name = "customer_id"),
            inverseJoinColumns = @JoinColumn(name = "post_id"))
    @ToString.Exclude
    private Set<Post> likesPosts = new HashSet<>();

    @ManyToMany(cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    @JoinTable(name = "users_dislikes",
            joinColumns = @JoinColumn(name = "customer_id"),
            inverseJoinColumns = @JoinColumn(name = "post_id"))
    @ToString.Exclude
    private Set<Post> dislikesPosts = new HashSet<>();

    @ManyToMany(cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    @JoinTable(
            name = "user_subscribers",
            joinColumns = { @JoinColumn(name = "channel_id") },
            inverseJoinColumns = { @JoinColumn(name = "subscriber_id") }
    )
    @ToString.Exclude
    private Set<User> subscribers = new HashSet<>();

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "user_subscriptions",
            joinColumns = { @JoinColumn(name = "subscriber_id") },
            inverseJoinColumns = { @JoinColumn(name = "channel_id") }
    )
    @ToString.Exclude
    private Set<User> subscriptions = new HashSet<>();



    public User(long l, String test, String s, String s1) {
    }



    public Boolean isSubscriptions(User user) {
        return subscriptions.contains(user);
    }

    public void addSubscribers(User user) {
        subscribers.add(user);
    }

    public void removeSubscribers(User user) {
        subscribers.remove(user);
    }

    public void addSubscriptions(User user) {
        subscriptions.add(user);
    }

    public void removeSubscriptions(User user) {
        subscriptions.remove(user);
    }

    public Boolean addLikedPost(Post post) {
        return likesPosts.add(post);
    }

    public Boolean removeLikedPost(Post post) {
        return likesPosts.remove(post);
    }

    public Boolean addDislikedPost( Post post) {
        return dislikesPosts.add(post);
    }

    public Boolean removeDislikedPost(Post post) {
        return dislikesPosts.remove(post);
    }

    public Boolean hasLikedPost(Post post) {
        return likesPosts.contains(post);
    }

    public Boolean hasDislikedPost(Post post) {
        return dislikesPosts.contains(post);
    }



}
