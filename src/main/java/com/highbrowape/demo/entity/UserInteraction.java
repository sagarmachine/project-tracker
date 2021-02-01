package com.highbrowape.demo.entity;


import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.util.Date;

@Entity
@Data
@Builder
@AllArgsConstructor

public class UserInteraction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id;
    
    int date;
    int day;
    int month;
    int year;

    int count;

    @CreationTimestamp
    Date createdOn;


    @CreationTimestamp
    @JsonFormat(pattern = "hh.MM", timezone = "IST")
    Date time;



    @ManyToOne
    User user;

    public UserInteraction() {

    }
}
