package com.highbrowape.demo.dto.output;


import com.highbrowape.demo.entity.Mission;
import lombok.Data;

import javax.persistence.Column;
import java.util.List;
import java.util.Set;

@Data
public class ProjectDashboardDto {

    long id;

    String projectId;

    String projectName;

    Set<Mission> missions;


}
