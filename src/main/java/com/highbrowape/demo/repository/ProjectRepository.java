package com.highbrowape.demo.repository;

import com.highbrowape.demo.dto.output.ProjectListDto;
import com.highbrowape.demo.entity.Project;
import com.highbrowape.demo.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProjectRepository extends JpaRepository<Project,Long> {


    Optional<Project> findByUserAndProjectId(User user, String projectId);
}
