package com.highbrowape.demo.repository;

import com.highbrowape.demo.entity.Project;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjectRepository extends JpaRepository<Project,Long> {
    
}
