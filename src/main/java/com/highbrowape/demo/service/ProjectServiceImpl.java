package com.highbrowape.demo.service;


import com.highbrowape.demo.dto.input.ProjectAdd;
import com.highbrowape.demo.dto.input.ProjectUpdate;
import com.highbrowape.demo.dto.output.ProjectDashboardDto;
import com.highbrowape.demo.dto.output.ProjectListDto;
import com.highbrowape.demo.entity.Project;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class ProjectServiceImpl implements IProjectService {
    @Override
    public ResponseEntity<?> addProject(ProjectAdd projectAdd, String loggedInEmail) {
        return null;
    }

    @Override
    public ResponseEntity<?> updateProject(ProjectUpdate projectUpdate, String loggedInEmail) {
        return null;
    }

    @Override
    public ResponseEntity<Project> getProjectDetail(Long projectId, String loggedInEmail) {
        return null;
    }

    @Override
    public ResponseEntity<ProjectDashboardDto> getProjectDashboard(Long projectId, String loggedInEmail) {
        return null;
    }

    @Override
    public ResponseEntity<ProjectListDto> getProjectList(int pageNumber, String loggedInEmail) {
        return null;
    }

}
