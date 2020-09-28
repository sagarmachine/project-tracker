package com.highbrowape.demo.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.util.Date;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Progress {

    @Id
    @GeneratedValue
    long id;

    @Column(nullable = false)
    String heading;

    @Column(nullable = false)
    String description;

    @CreationTimestamp
    @JsonFormat(pattern = "yyyy-MM-dd")
    Date addedOn;

    @CreationTimestamp
    @JsonFormat(pattern = "yyyy-MM-dd")
    Date updatedOn;

    String addedBy;

    @ManyToOne
    @JoinColumn
    @JsonIgnore
    Mission mission;

}
