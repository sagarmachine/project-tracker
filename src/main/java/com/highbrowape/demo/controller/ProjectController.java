package com.highbrowape.demo.controller;


import com.highbrowape.demo.dto.ProjectAdd;
import com.highbrowape.demo.dto.ProjectUpdate;
import com.highbrowape.demo.service.IProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequestMapping(value = "/api/v1/project")
public class ProjectController {

    @Autowired
    IProjectService projectService;


    @PostMapping("")
    public ResponseEntity<?>  addProject(@RequestBody ProjectAdd projectAdd, Principal principal){

        return projectService.addProject(projectAdd,principal.getName());

    }

    @PostMapping("")
    public ResponseEntity<?>  updateProject(@RequestBody ProjectUpdate projectUpdate, Principal principal){

        return projectService.updateProject(projectUpdate,principal.getName());

    }


}
