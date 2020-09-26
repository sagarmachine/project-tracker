package com.highbrowape.demo.service;


import com.highbrowape.demo.dto.ProjectAdd;
import com.highbrowape.demo.dto.ProjectUpdate;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class ProjectServiceImpl implements IProjectService {
    @Override
    public ResponseEntity<?> addProject(ProjectAdd projectAdd, String creatorEmail) {
        return null;
    }

    @Override
    public ResponseEntity<?> updateProject(ProjectUpdate projectUpdate, String creatorEmail) {
        return null;
    }

}
