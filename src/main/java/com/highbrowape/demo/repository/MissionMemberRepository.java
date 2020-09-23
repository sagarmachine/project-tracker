package com.highbrowape.demo.repository;

import com.highbrowape.demo.entity.MissionMember;
import com.highbrowape.demo.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MissionMemberRepository extends JpaRepository<MissionMember,Long> {

}
