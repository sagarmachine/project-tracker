package com.highbrowape.demo.repository;

import com.highbrowape.demo.entity.Mission;
import com.highbrowape.demo.entity.Project;
import com.highbrowape.demo.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MissionRepository extends JpaRepository<Mission,Long> {


    long countByProjectAndLevel(Project project, long level);

    Optional<Mission> findByMissionId(String id);

    long countByMissionParent(Mission missionParent);

    List<Mission> findByProjectProjectIdAndLevel(String projectId, long level);

    int countByProject(Project project);

}
