package com.highbrowape.demo.dto.output;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.highbrowape.demo.entity.Category;
import com.highbrowape.demo.entity.MissionInsight;
import com.highbrowape.demo.entity.Priority;
import com.highbrowape.demo.entity.Status;
import lombok.Data;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@Data
public class MissionDto {
    long id;

    String name;

    String missionId;

    String description;

    long level;

    @JsonFormat(pattern = "yyyy-MM-dd")
    Date addedOn;

    @JsonFormat(pattern = "yyyy-MM-dd")
    Date updatedOn;

    String addedBy;

    Status status;

    Priority priority;

    Category category;

    Date startingDate;

    Date endingDate;

    String parentId;

    List<String> childIds;

    int memberCount;

    int linkCount;

    int noteCount;

//    int objectiveCount;
//    int completedObjectiveCount;

    int conversationCount;

    List<MissionDto> children= new ArrayList<>();

    MissionInsight missionInsight;


}
