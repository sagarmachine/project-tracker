package com.highbrowape.demo.entity;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
public class ProjectNote {

    @Id
    @GeneratedValue
    long id;

    @Column(nullable = false)
    String title;

    @Column(nullable = false)
    String description;

    @CreationTimestamp
    @JsonFormat(pattern = "yyyy-MM-dd")
    Date addedOn;

    @CreationTimestamp
    @JsonFormat(pattern = "yyyy-MM-dd")
    Date updatedOn;

    @Column(nullable = false)
    String addedBy;

    @ManyToOne
    @JoinColumn
    @JsonIgnore
    Project project;

}
