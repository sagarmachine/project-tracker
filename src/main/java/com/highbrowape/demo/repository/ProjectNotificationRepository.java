package com.highbrowape.demo.repository;

import com.highbrowape.demo.entity.ProjectNotification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface ProjectNotificationRepository extends JpaRepository<ProjectNotification, Long> {

    List<ProjectNotification> findAllByProjectProjectId(String id);
}
