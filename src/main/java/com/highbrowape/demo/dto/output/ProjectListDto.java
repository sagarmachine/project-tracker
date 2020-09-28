package com.highbrowape.demo.dto.output;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import java.util.Date;

@Getter
@Setter
public class ProjectListDto {

    long id;

    long projectId;

    String projectName;

    String description;

    Date startingDate;

    Date endingDate;

    long totalMissions;

    long totalMembers;

}
