package com.highbrowape.demo.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;

@Entity
@Data
public class MissionMemberInsight {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    int objectiveCount;

    int completedObjectiveCount;

    @OneToOne(mappedBy = "missionMemberInsight")
    @JsonIgnore
    MissionMember missionMember;

    int noteCount;

    int linkCount;

}
