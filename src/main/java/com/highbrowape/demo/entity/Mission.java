package com.highbrowape.demo.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Mission implements Serializable {

    @Id
    @GeneratedValue
    long id;

    String addedBy;

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

    @UpdateTimestamp
    @JsonFormat(pattern = "yyyy-MM-dd")
    Date updatedOn;




    @Enumerated(EnumType.STRING)
    Status status;

    @Enumerated(EnumType.STRING)
    Priority priority;

    @Enumerated(EnumType.STRING)
    Category category;

    @JsonFormat(pattern = "yyyy-MM-dd")
    Date startingDate;

    @JsonFormat(pattern = "yyyy-MM-dd")
    Date endingDate;


    @OneToOne
    @JoinColumn
    MissionInsight missionInsight;

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
    List<MissionMember> missionMembers=new ArrayList<>();

    @OneToMany(mappedBy = "mission",cascade = {CascadeType.ALL})
    List<Note> missionNotes= new ArrayList<>();

    @OneToMany(mappedBy = "mission",cascade = {CascadeType.ALL})
    List<Link> missionLinks=new ArrayList<>();

    @OneToMany(mappedBy = "mission",cascade = {CascadeType.ALL})
    List<Progress> progress= new ArrayList<>();

    @OneToMany(mappedBy = "mission",cascade = {CascadeType.ALL})
    List<Objective> objectives= new ArrayList<>();

    @OneToMany(mappedBy = "mission",cascade = {CascadeType.ALL})
    List<Conversation> conversations= new ArrayList<>();


    public void addMissionMember(MissionMember missionMember){
        missionMembers.add(missionMember);
        missionMember.setMission(this);
    }

    public void addLink(Link link){
        missionLinks.add(link);
        link.setMission(this);
    }
    public void addNote(Note note){
        missionNotes.add(note);
        note.setMission(this);
    }
    public void addConversations(Conversation conversation){
        conversations.add(conversation);
        conversation.setMission(this);
    }
    public void addObjective(Objective objective){
        objectives.add(objective);
        objective.setMission(this);
    }
    public void addProgress(Progress progress){
        this.progress.add(progress);
        progress.setMission(this);
    }
}

