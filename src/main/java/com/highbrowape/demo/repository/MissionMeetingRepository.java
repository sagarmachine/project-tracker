package com.highbrowape.demo.repository;

import com.highbrowape.demo.entity.Mission;
import com.highbrowape.demo.entity.MissionMeeting;
import com.highbrowape.demo.entity.MissionMember;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MissionMeetingRepository extends JpaRepository<MissionMeeting,Long> {


    Optional<MissionMeeting> findByMission(Mission mission);
}
