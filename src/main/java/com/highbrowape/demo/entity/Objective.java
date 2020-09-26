package com.highbrowape.demo.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Objective {

    @Id
    @GeneratedValue
    long id;

    @Column(nullable = false)
    String description;

    @Enumerated(EnumType.STRING)
    Status status;

    @Enumerated(EnumType.STRING)
    Priority priority;

    @Enumerated(EnumType.STRING)
    Category category;

    @ManyToOne
    @JoinColumn
    @JsonIgnore
    Mission mission;
}
