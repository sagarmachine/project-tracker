package com.highbrowape.demo.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;
import java.util.Set;


@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Member {

    @Id
    @GeneratedValue
    long id;

    @Column(nullable = false)
    String email;

    @Enumerated(EnumType.STRING)
    Authority authority;

    @JsonFormat(pattern = "yyyy-MM-dd")
    @CreationTimestamp
    Date addedOn;


    String addedBy;

    @ManyToOne(cascade = {CascadeType.ALL})
    @JoinColumn
    @JsonIgnore
    Project project;

    @OneToMany(mappedBy = "member",cascade = {CascadeType.ALL})
    List<MissionMember> missionMembers;


}

