package com.highbrowape.demo.controller;


import com.highbrowape.demo.dto.input.ProjectAdd;
import com.highbrowape.demo.dto.input.ProjectUpdate;
import com.highbrowape.demo.dto.output.ProjectDashboardDto;
import com.highbrowape.demo.dto.output.ProjectListDto;
import com.highbrowape.demo.entity.Project;
import com.highbrowape.demo.repository.ProjectRepository;
import com.highbrowape.demo.service.IProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping(value = "/api/v1/project")
public class ProjectController {

    @Autowired
    IProjectService projectService;

    @Autowired
    ProjectRepository projectRepository;


    @PostMapping("")
    public ResponseEntity<?>  addProject(@RequestBody ProjectAdd projectAdd, Principal principal){

        return projectService.addProject(projectAdd,principal.getName());

    }

    @PutMapping("")
    public ResponseEntity<?>  updateProject(@RequestBody ProjectUpdate projectUpdate, Principal principal){

        return projectService.updateProject(projectUpdate,principal.getName());

    }

    @GetMapping("/list/{pageNumber}")
    public ResponseEntity<ProjectListDto>  getProjectList(@PathVariable("pageNumber")int pageNumber, Principal principal){

        return projectService.getProjectList(pageNumber,principal.getName());

    }

    @GetMapping("/dashboard/{id}")
    public  ResponseEntity<ProjectDashboardDto> getProjectDashboard(@PathVariable("id")Long id, Principal principal){
        return projectService.getProjectDashboard(id,principal.getName());
    }

    @GetMapping("/detail/{id}")
    public  ResponseEntity<Project> getProjectDetailView(@PathVariable("id")Long id, Principal principal){
        return projectService.getProjectDetail(id,principal.getName());
    }

}
