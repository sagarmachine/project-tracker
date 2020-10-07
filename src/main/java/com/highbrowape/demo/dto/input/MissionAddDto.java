package com.highbrowape.demo.dto.input;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.highbrowape.demo.entity.Category;
import com.highbrowape.demo.entity.Priority;
import lombok.Data;

import java.util.Date;
import java.util.List;


@Data
public class MissionAddDto {

    String name;


    String description;

    Priority priority;

    Category category;

    @JsonFormat(pattern = "yyyy-MM-dd")
    Date startingDate;

    @JsonFormat(pattern = "yyyy-MM-dd")
    Date endingDate;


    List<NoteDto> notes;

    List<LinkDto> links;

    List<ProjectMemberDto> member;


}
