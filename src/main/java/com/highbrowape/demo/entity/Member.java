package com.highbrowape.demo.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Set;


@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Member {

    @Id
    @GeneratedValue
    long id;

    String email;

    @ManyToOne
    @JoinColumn
    User user;

    @Enumerated(EnumType.STRING)
    Authority authority;

    @JsonFormat(pattern = "yyyy-MM-dd")
    @CreationTimestamp
    Date addedOn;
    @CreationTimestamp
    @JsonFormat(pattern = "yyyy-MM-dd")
    Date updatedOn;

    String addedBy;

    @ManyToOne(cascade = {CascadeType.PERSIST})
    @JoinColumn
    @JsonIgnore
    Project project;

    @OneToMany(mappedBy = "member",cascade = {CascadeType.ALL})
    List<MissionMember> missionMembers;


    public Member(String addedBy, Project project, User user, Authority authority, Date addedOn) {
        this.addedBy=addedBy;
        this.project=project;
        this.user=user;
        this.authority=authority;
        this.addedOn=addedOn;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Member)) return false;
        Member member = (Member) o;
        return getId() == member.getId() &&
                Objects.equals(getProject(), member.getProject());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getProject());
    }
}

