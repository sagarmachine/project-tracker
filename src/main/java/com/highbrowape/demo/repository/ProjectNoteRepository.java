package com.highbrowape.demo.repository;

import com.highbrowape.demo.entity.ProjectLink;
import com.highbrowape.demo.entity.ProjectNote;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjectNoteRepository extends JpaRepository<ProjectNote,Long> {

}
