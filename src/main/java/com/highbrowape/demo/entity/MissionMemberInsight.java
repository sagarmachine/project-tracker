package com.highbrowape.demo.entity;


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
    MissionMember missionMember;

    @ManyToOne
    @JoinColumn
    Mission mission;

}
