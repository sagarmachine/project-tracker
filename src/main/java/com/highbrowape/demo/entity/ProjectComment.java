package com.highbrowape.demo.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class ProjectComment implements Serializable {

    @Id
    @GeneratedValue
    long id;

    @CreationTimestamp
    @JsonFormat(pattern = "E, dd MMM yyyy::HH:mm:ss z", timezone = "IST")
    Date addedOn;

//    @CreationTimestamp
//    @JsonFormat(pattern = "MM", timezone = "IST")
    int month;

//    @CreationTimestamp
//    @JsonFormat(pattern = "dd", timezone = "IST")
    int date;

    @Column(nullable = false)
    String addedBy;

    @Column(nullable = false)
    String comment;

    @Column(nullable = false)
    String description;

    String addedByImage;


    @ManyToOne
    @JoinColumn
    @JsonIgnore
    ProjectConversation projectConversation;

}
