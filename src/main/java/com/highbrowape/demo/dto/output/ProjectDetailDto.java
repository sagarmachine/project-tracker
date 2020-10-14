package com.highbrowape.demo.dto.output;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.highbrowape.demo.entity.ProjectInsight;
import lombok.Data;
import java.util.Date;

@Data
public class ProjectDetailDto  {
    long id;

    String projectId;

    String projectName;

    String description;


    @JsonFormat(pattern = "dd-MM-yyy")
    Date addedOn;

    @JsonFormat(pattern = "dd-MM-yyy")
    Date updatedOn;

    @JsonFormat(pattern = "dd-MM-yyy")
    Date startingDate;

    @JsonFormat(pattern = "dd-MM-yyy")
    Date endingDate;

    String imageUrl;
    String thumbnailUrl;
    String deleteUrl;

     String createBy;

    ProjectInsight projectInsight;

    int memberCount;

    int missionCount;

    int soldierCount;

    int chiefCount;

}
