package com.highbrowape.demo.repository;

import com.highbrowape.demo.entity.Link;
import com.highbrowape.demo.entity.Project;
import com.highbrowape.demo.entity.ProjectLink;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
public interface ProjectLinkRepository extends JpaRepository<ProjectLink,Long> {


    void deleteByProject(Project project);

    void deleteAllByProject(Project project);

    List<ProjectLink> findByProjectProjectId(Long id);
}
