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
import java.util.List;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Project {

    @Id
    @GeneratedValue
    long id;

    @Column(nullable = false,unique=true)
    long projectId;

    @Column(nullable = false)
    String projectName;

    @Column(nullable = false)
    String description;

    @CreationTimestamp
    @JsonFormat(pattern = "yyyy-MM-dd")
    Date addedOn;

    @JsonFormat(pattern = "yyyy-MM-dd")
    @Column(nullable = false)
    Date startingDate;

    @JsonFormat(pattern = "yyyy-MM-dd")
    @Column(nullable = false)
    Date endingDate;

    byte[] image;

    @ManyToOne
    @JoinColumn
    @JsonIgnore
    User user;

    @OneToMany(mappedBy = "project",cascade = {CascadeType.ALL},fetch = FetchType.EAGER)
    List<Member> members;

    @OneToMany(mappedBy = "project",cascade = {CascadeType.ALL})
    List<Mission> missions;


}
