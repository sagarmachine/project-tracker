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
public class ProjectConversation {

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

    @OneToMany(mappedBy = "projectConversation",cascade = {CascadeType.ALL})
    List<ProjectComment> projectComments= new ArrayList<ProjectComment>();

    @ManyToOne
    @JoinColumn
    @JsonIgnore
    Project project;

    public void addComment(ProjectComment comment){
        projectComments.add(comment);
        comment.setProjectConversation(this);
    }

}
