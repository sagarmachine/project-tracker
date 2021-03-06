package com.highbrowape.demo.entity;


import lombok.Data;
import net.minidev.json.annotate.JsonIgnore;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Data
public class ProjectNotification implements Serializable {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id;


    @CreationTimestamp
    Date createdOn;

    @ManyToOne
    @JsonIgnore
    Project project;

    String message;

}
