package com.highbrowape.demo.dto.output;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.highbrowape.demo.entity.Mission;
import com.highbrowape.demo.entity.ProjectInsight;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
public class ProjectListDto {

    long id;

    String projectId;

    String projectName;

    String description;

    Date startingDate;

    Date endingDate;

    long totalMissions;

    long totalMembers;

    ProjectInsight projectInsight;

    Set<Mission> missions= new HashSet<>();


}
