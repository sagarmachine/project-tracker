package com.highbrowape.demo.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Conversation {

    @Id
    @GeneratedValue
    long id;

    @Column(nullable = false)
    String title;

    @Column(nullable = false)
    String conversation;

    @CreationTimestamp
    @JsonFormat(pattern = "yyyy-MM-dd")
    Date StartedOn;

    @Column(nullable = false)
    String StartedBy;

    @Column(nullable = false)
    String startedOver;

    @OneToMany(mappedBy = "conversation",cascade = {CascadeType.ALL})
    List<Comment> comments;

    @ManyToOne
    @JoinColumn
    Mission mission;

}
