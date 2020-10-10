package com.highbrowape.demo.entity;


import lombok.Data;

import javax.persistence.*;

@Data
@Entity
public class UserInsights {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id;

    int objectiveCount;

    int completedObjectiveCount;

    @OneToOne(mappedBy = "userInsights")
    User user ;



}
