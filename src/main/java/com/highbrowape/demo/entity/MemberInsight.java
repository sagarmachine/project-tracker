package com.highbrowape.demo.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;

@Entity
@Data
public class MemberInsight {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    int objectiveCount;

    int completedObjectiveCount;

    @OneToOne(mappedBy = "memberInsight")
    @JsonIgnore
    Member member;

    int noteCount;

    int linkCount;



}
