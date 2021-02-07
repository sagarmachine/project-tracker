package com.highbrowape.demo.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Service;

import javax.persistence.Column;
import javax.persistence.Embeddable;



@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Embeddable

public class MeetingMember {


    @Column(nullable = false)
    String email;

    @Column(nullable = false)
    String name;

    @Column(nullable = false)
    String imagUrl;

}


