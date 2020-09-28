package com.highbrowape.demo.entity;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.util.Date;
import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ProjectNote)) return false;
        ProjectNote that = (ProjectNote) o;
        return getId() == that.getId() &&
                Objects.equals(getProject(), that.getProject());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getProject());
    }
}
