package com.highbrowape.demo.repository;

import com.highbrowape.demo.entity.Project;
import com.highbrowape.demo.entity.ProjectLink;
import com.highbrowape.demo.entity.ProjectNote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
public interface ProjectNoteRepository extends JpaRepository<ProjectNote,Long> {


    void deleteByProject(Project project);

    void deleteAllByProject(Project project);

    List<ProjectNote> findByProjectProjectId(String id);
}
