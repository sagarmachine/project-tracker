package com.highbrowape.demo.repository;

import com.highbrowape.demo.entity.Link;
import com.highbrowape.demo.entity.ProjectLink;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjectLinkRepository extends JpaRepository<ProjectLink,Long> {

}
