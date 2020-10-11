package com.highbrowape.demo.entity;


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
    Member member;

    @ManyToOne
    @JoinColumn
    Project project;




}
