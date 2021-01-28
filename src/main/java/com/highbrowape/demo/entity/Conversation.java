package com.highbrowape.demo.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.util.ArrayList;
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

    @CreationTimestamp()
    @JsonFormat(pattern = "E, dd MMM yyyy::HH:mm:ss z", timezone = "IST")
    Date startedOn;

    int month;


    int date;

    String startedByImage;

    @Column(nullable = false)
    String startedBy;

    @Column(nullable = false)
    String startedOver="null";

    @OneToMany(mappedBy = "conversation",cascade = {CascadeType.ALL})
    List<Comment> comments= new ArrayList<Comment>();

    @ManyToOne
    @JoinColumn
    @JsonIgnore
    Mission mission;

    public void addComment(Comment comment){
        comments.add(comment);
        comment.setConversation(this);
    }

}
