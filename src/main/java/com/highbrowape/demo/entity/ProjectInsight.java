package com.highbrowape.demo.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;

@Entity
@Data
public class ProjectInsight {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id;

    int objectiveCount;

    int completedObjectiveCount;

    @OneToOne(mappedBy = "projectInsight", cascade = CascadeType.ALL)
    @JsonIgnore
    Project project;

    int noteCount;

    int linkCount;

}
