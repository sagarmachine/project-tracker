package com.highbrowape.demo.entity;


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
    Project project;

}
