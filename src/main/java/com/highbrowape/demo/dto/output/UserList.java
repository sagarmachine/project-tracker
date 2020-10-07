package com.highbrowape.demo.dto.output;


import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.persistence.Column;
import java.util.Date;

@Data
public class UserList {

    String email;

    String firstName;

    String lastName;

    String imageUrl;

    String thumbnailUrl;




    @JsonFormat(pattern = "yyyy-MM-dd")
    Date addedOn;

    public UserList(String email, String firstName, String lastName, Date addedOn,String imageUrl,String thumbnailUrl) {
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.addedOn = addedOn;
        this.imageUrl=imageUrl;
        this.thumbnailUrl=thumbnailUrl;
    }
}
