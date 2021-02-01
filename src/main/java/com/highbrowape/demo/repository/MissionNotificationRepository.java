package com.highbrowape.demo.repository;

import com.highbrowape.demo.entity.MissionNotification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MissionNotificationRepository extends JpaRepository<MissionNotification, Long> {

    List<MissionNotification> findAllByMissionMissionId(String missionId);

}
