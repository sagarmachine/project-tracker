package com.highbrowape.demo.dto.output;


import com.highbrowape.demo.entity.Mission;
import com.highbrowape.demo.entity.ProjectInsight;
import lombok.Data;
import java.util.List;

@Data
public class ProjectDashboardDto {

    long id;

    String projectId;

    String projectName;

    ProjectInsight projectInsight;


}
