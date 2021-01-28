package com.highbrowape.demo.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id;

    @Column(nullable = false,unique = true)
    String email;

    @Column(nullable = false)
    String firstName;

    String lastName;

     String password;


     @Lob
    String imageUrl;

    String thumbnailUrl;

    String deleteUrl;

    @Column(unique=true,length=10)
    String phoneNumber;

    @CreationTimestamp
    @JsonFormat(pattern = "yyyy-MM-dd")
    Date addedOn;


    @OneToOne(cascade =CascadeType.ALL)
    @JoinColumn
    UserInsights userInsights;




    @OneToMany(mappedBy = "user",cascade = {CascadeType.ALL})
    @JsonIgnore
    Set<Project> projects;

    @OneToMany(mappedBy = "user",cascade = {CascadeType.ALL})
    @JsonIgnore
    Set<Member> members;

    @OneToMany(mappedBy = "user", cascade=CascadeType.ALL)
    Set<UserAction> userActions= new HashSet<>();

    @OneToMany(mappedBy = "user",cascade = {CascadeType.ALL})
    @JsonIgnore
    Set<UserInteraction> userInteractions= new HashSet<>();

    public void addUserInteraction(UserInteraction userInteraction){
        userInteractions.add(userInteraction);
        userInteraction.setUser(this);
    }

    public void addUserAction(UserAction userAction){

        userActions.add(userAction);
        userAction.setUser(this);

    }



}
