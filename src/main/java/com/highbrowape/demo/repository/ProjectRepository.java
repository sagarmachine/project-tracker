package com.highbrowape.demo.repository;


import com.highbrowape.demo.entity.Project;
import com.highbrowape.demo.entity.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProjectRepository extends JpaRepository<Project,Long> {


    List<Project> findByUserEmail(String loggedInEmail);

    List<Project> findByUserEmail(String loggedInEmail, Pageable pageable);
//    List<Project> findByMe(String loggedInEmail, Pageable pageable);



    Optional<Project> findByUserAndProjectId(User user, String projectId);


    double countByUserEmail(String loggedInEmail);

    Optional<Project> findByProjectId(String projectId);
}
