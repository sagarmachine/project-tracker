package com.highbrowape.demo.service;


import com.highbrowape.demo.dto.ProjectAdd;
import com.highbrowape.demo.dto.ProjectUpdate;
import org.springframework.http.ResponseEntity;

public interface IProjectService {

    ResponseEntity<?>  addProject(ProjectAdd projectAdd, String loggedInEmail);
    ResponseEntity<?>  updateProject(ProjectUpdate projectUpdate, String loggedInEmail);

}
