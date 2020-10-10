package com.highbrowape.demo.entity;


import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.util.Date;

@Entity
@Data
public class UserAction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id;

    String comment;

    @CreationTimestamp
    Date date;

    @ManyToOne
    @JoinColumn
    User user;


}
