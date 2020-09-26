package com.highbrowape.demo.dto.output;


import com.highbrowape.demo.entity.Mission;
import lombok.Data;

import javax.persistence.Column;
import java.util.List;

@Data
public class ProjectDashboardDto {

    String id;

    long projectId;

    String projectName;

    List<Mission> missions;


}
