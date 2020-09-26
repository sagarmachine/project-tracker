package com.highbrowape.demo.dto.input;


import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class ProjectAdd {


    String projectName;

    int projectId;

    String description;

    @JsonFormat(pattern = "yyyy-MM-dd")
    Date startingDate;

    @JsonFormat(pattern = "yyyy-MM-dd")
    Date endingDate;


    List<ProjectMemberDto> members;



}
