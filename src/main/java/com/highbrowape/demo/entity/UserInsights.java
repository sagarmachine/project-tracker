package com.highbrowape.demo.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
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
   @JsonIgnore
    User user ;



}
