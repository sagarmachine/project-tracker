package com.highbrowape.demo.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;


@Entity
@Data
public class MissionInsight implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id;

    int objectiveCount;

    int completedObjectiveCount;

    @OneToOne(mappedBy = "missionInsight", cascade = CascadeType.ALL)
    @JsonIgnore
    Mission mission;
}
