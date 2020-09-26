package com.highbrowape.demo.dto;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.highbrowape.demo.entity.Member;
import com.highbrowape.demo.entity.Mission;
import com.highbrowape.demo.entity.User;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class ProjectAdd {


    String projectName;

    String description;

    @JsonFormat(pattern = "yyyy-MM-dd")
    Date startingDate;

    @JsonFormat(pattern = "yyyy-MM-dd")
    Date endingDate;


    List<ProjectMemberDto> members;



}
