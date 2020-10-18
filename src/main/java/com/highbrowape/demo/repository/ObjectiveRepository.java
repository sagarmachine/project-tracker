package com.highbrowape.demo.repository;

import com.highbrowape.demo.entity.Mission;
import com.highbrowape.demo.entity.Objective;
import com.highbrowape.demo.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ObjectiveRepository extends JpaRepository<Objective,Long> {

    Object findByMissionMissionId(String id);

    List<Objective> findByMission(Mission mission);
}
