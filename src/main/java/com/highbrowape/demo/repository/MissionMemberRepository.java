package com.highbrowape.demo.repository;

import com.highbrowape.demo.entity.Mission;
import com.highbrowape.demo.entity.MissionMember;
import com.highbrowape.demo.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MissionMemberRepository extends JpaRepository<MissionMember,Long> {

    Optional<MissionMember> findByMissionAndMemberUserEmail(Mission mission, String email);

    Optional<MissionMember> findByMissionMissionIdAndMemberUserEmail(String missionId, String email);
}
