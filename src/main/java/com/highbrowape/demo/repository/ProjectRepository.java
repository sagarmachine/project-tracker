package com.highbrowape.demo.repository;

import com.highbrowape.demo.dto.output.ProjectListDto;
import com.highbrowape.demo.entity.Project;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProjectRepository extends JpaRepository<Project,Long> {


}
