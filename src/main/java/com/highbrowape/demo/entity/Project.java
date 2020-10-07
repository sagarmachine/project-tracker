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
import java.util.*;


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
    String projectId;

    @Column(nullable = false)
    String projectName;

    @Column(nullable = false)
    String description;

    @CreationTimestamp
    @JsonFormat(pattern = "yyyy-MM-dd")
    Date addedOn;

    @CreationTimestamp
    @JsonFormat(pattern = "yyyy-MM-dd")
    Date updatedOn;

    @JsonFormat(pattern = "yyyy-MM-dd")
    @Column(nullable = false)
    Date startingDate;

    @JsonFormat(pattern = "yyyy-MM-dd")
    @Column(nullable = false)
    Date endingDate;

    String imageUrl;
    String thumbnailUrl;
    String deleteUrl;

    @ManyToOne
    @JoinColumn
    @JsonIgnore
    User user;

    @OneToMany(mappedBy = "project",cascade = {CascadeType.ALL},fetch = FetchType.EAGER)
    Set<Member> members= new HashSet<>();

    @OneToMany(mappedBy = "project",cascade = {CascadeType.ALL},fetch = FetchType.EAGER)
    Set<ProjectLink> projectLinks= new HashSet<>();

    @OneToMany(mappedBy = "project",cascade = {CascadeType.ALL},fetch = FetchType.EAGER)
    Set<ProjectNote> projectNotes= new HashSet<>();

    @OneToMany(mappedBy = "project",cascade = {CascadeType.ALL})
    Set<Mission> missions= new HashSet<>();



    public void addMember(Member member){
        members.add(member);
        member.setProject(this);
    }

    public void addLink(ProjectLink projectLink){
        projectLinks.add(projectLink);
        projectLink.setProject(this);
    }
    public void addNote(ProjectNote projectNote){
        projectNotes.add(projectNote);
        projectNote.setProject(this);
    }
    public void addMission(Mission mission){
        missions.add(mission);
        mission.setProject(this);
    }



}
