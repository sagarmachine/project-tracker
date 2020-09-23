package com.highbrowape.demo.repository;

import com.highbrowape.demo.entity.Mission;
import com.highbrowape.demo.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MissionRepository extends JpaRepository<Mission,Long> {

}
