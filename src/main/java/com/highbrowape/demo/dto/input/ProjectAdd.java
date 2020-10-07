package com.highbrowape.demo.dto.input;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.highbrowape.demo.entity.User;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class ProjectAdd {


    String projectName;

    String projectId;

    String description;

    @JsonFormat(pattern = "yyyy-MM-dd")
    Date startingDate;

    @JsonFormat(pattern = "yyyy-MM-dd")
    Date endingDate;

    User user;

    Date addedOn;

    String imageUrl;
    String thumbnailUrl;
    String deleteUrl;

    List<NoteDto> notes;

    List<LinkDto> links;

    List<ProjectMemberDto> member;

}
