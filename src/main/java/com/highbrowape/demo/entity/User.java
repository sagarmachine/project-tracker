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

    @Column(nullable = false)
    String lastName;

    @Column(nullable = false)
     String password;


    String imageUrl;

    String thumbnailUrl;

    String deleteUrl;

    @Column(unique=true,length=10)
    String phoneNumber;

    @OneToMany(mappedBy = "user",cascade = {CascadeType.ALL})
    @JsonIgnore
    Set<Project> projects;

    @OneToMany(mappedBy = "user",cascade = {CascadeType.ALL})
    @JsonIgnore
    Set<Member> members;

    @CreationTimestamp
    @JsonFormat(pattern = "yyyy-MM-dd")
    Date addedOn;


}
