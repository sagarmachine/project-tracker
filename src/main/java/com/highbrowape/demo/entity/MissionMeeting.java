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
import java.util.*;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class MissionMeeting {
    @Id
    @GeneratedValue
    long id;

    @Column(nullable = false, unique = true)
    String name;

    @Column(nullable = false)
    String title;

    @Column(nullable = false)
    String description;

    @JsonFormat(pattern = "yyyy-MM-dd")
    @CreationTimestamp
    Date addedOn;

    String addedBy;

    String password;

    @ElementCollection
    @CollectionTable(name = "mission_meeting_members", joinColumns = @JoinColumn(name = "mission_meeting_id"))
    Set<MeetingMember> meetingMembers=new HashSet<MeetingMember>();

    @OneToOne
    @JoinColumn
    @JsonIgnore
    Mission mission;

    public void addMeetingMember(MeetingMember meetingMember){
        meetingMembers.add(meetingMember);

    }

    public void removeMeetingMember(MeetingMember meetingMember){
        meetingMembers.remove(meetingMember);

    }
}
