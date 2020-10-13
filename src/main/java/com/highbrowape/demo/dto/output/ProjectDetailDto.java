package com.highbrowape.demo.dto.output;

import com.highbrowape.demo.entity.ProjectInsight;
import lombok.Data;
import java.util.Date;

@Data
public class ProjectDetailDto  {
    long id;

    String projectId;

    String projectName;

    String description;

    Date addedOn;

    Date updatedOn;

    Date startingDate;

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
