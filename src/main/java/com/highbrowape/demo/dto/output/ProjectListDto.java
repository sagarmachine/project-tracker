package com.highbrowape.demo.dto.output;

import com.fasterxml.jackson.annotation.JsonFormat;

import javax.persistence.Column;
import java.util.Date;

public class ProjectListDto {

    long projectId;

    String projectName;

    String description;

    Date startingDate;

    Date endingDate;

    int totalMissions;

    int totalMembers;

}
