package com.highbrowape.demo.service;


import com.highbrowape.demo.dto.input.ProjectAdd;
import com.highbrowape.demo.dto.input.ProjectUpdate;
import com.highbrowape.demo.dto.output.ProjectDashboardDto;
import com.highbrowape.demo.dto.output.ProjectListDto;
import com.highbrowape.demo.entity.Project;
import org.springframework.http.ResponseEntity;

public interface IProjectService {

    ResponseEntity<?>  addProject(ProjectAdd projectAdd, String loggedInEmail);
    ResponseEntity<?>  updateProject(ProjectUpdate projectUpdate, String loggedInEmail);
//    ResponseEntity<?>  getProject(Long projectId, String loggedInEmail);
    ResponseEntity<ProjectDashboardDto>  getProjectDashboard(Long projectId, String loggedInEmail);
    ResponseEntity<ProjectListDto> getProjectList(int pageNumber, String loggedInEmail);
    ResponseEntity<Project> getProjectDetail(Long projectId, String loggedInEmail);

}
