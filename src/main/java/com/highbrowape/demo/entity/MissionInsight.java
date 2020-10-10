package com.highbrowape.demo.entity;

import lombok.Data;

import javax.persistence.*;


@Entity
@Data
public class MissionInsight {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id;

    int objectiveCount;

    int completedObjectiveCount;

    @OneToOne(mappedBy = "missionInsight", cascade = CascadeType.ALL)
    Mission mission;
}
