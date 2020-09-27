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
import java.util.Set;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Mission {

    @Id
    @GeneratedValue
    long id;

    @Column(nullable = false)
    String name;

    @Column(nullable = false,unique=true)
    String missionId;

    @Column(nullable = false)
    String description;

    @Column(nullable = false)
    long level;

    @JsonFormat(pattern = "yyyy-MM-dd")
    @CreationTimestamp
    Date addedOn;

    @Column(nullable = false)
    String addedBy;

    @Enumerated(EnumType.STRING)
    Status status;

    @Enumerated(EnumType.STRING)
    Priority priority;

    @Enumerated(EnumType.STRING)
    Category category;


    @ManyToOne
    @JoinColumn
    @JsonIgnore
    Mission missionParent;

    @OneToMany(mappedBy = "missionParent",cascade = {CascadeType.ALL})
    Set<Mission> missions;

    @ManyToOne
    @JoinColumn
    @JsonIgnore
    Project project;

    @OneToMany(mappedBy = "mission",cascade = {CascadeType.ALL})
    List<MissionMember> missionMembers;

    @OneToMany(mappedBy = "mission",cascade = {CascadeType.ALL})
    List<Note> notes;

    @OneToMany(mappedBy = "mission",cascade = {CascadeType.ALL})
    List<Link> links;

    @OneToMany(mappedBy = "mission",cascade = {CascadeType.ALL})
    List<Progress> progress;

    @OneToMany(mappedBy = "mission",cascade = {CascadeType.ALL})
    List<Objective> objectives;

    @OneToMany(mappedBy = "mission",cascade = {CascadeType.ALL})
    List<Conversation> conversations;
}

