package com.highbrowape.demo.repository;

import com.highbrowape.demo.entity.Conversation;
import com.highbrowape.demo.entity.Mission;
import com.highbrowape.demo.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ConversationRepository extends JpaRepository<Conversation,Long> {

    List<Conversation> findByMission(Mission mission);


    List<Conversation> findByMissionOrderByStartedOnDesc(Mission mission);
}
