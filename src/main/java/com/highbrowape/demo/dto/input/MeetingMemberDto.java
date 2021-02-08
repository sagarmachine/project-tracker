package com.highbrowape.demo.dto.input;


import lombok.Data;


@Data

public class MeetingMemberDto {


    String email;

    String name;

    String imageUrl;

    public MeetingMemberDto(String email, String name, String imageUrl) {
        this.email = email;
        this.name = name;
        this.imageUrl = imageUrl;
    }
}
