package com.highbrowape.demo.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class MissionMember {

    @Id
    @GeneratedValue
    long id;

    @Enumerated(EnumType.STRING)
    Authority authority;

    @JsonFormat(pattern = "yyyy-MM-dd")
    Date addedOn;

    @Column(nullable = false)
    String addedBy;

    @ManyToOne
    @JoinColumn
    @JsonIgnore
    Mission mission;

    @ManyToOne
    @JoinColumn
    @JsonIgnore
    Member member;
}
