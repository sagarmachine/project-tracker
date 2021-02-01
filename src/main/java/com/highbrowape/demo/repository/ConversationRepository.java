package com.highbrowape.demo.repository;

import com.highbrowape.demo.dto.output.UserCommentChartData;
import com.highbrowape.demo.entity.Conversation;
import com.highbrowape.demo.entity.Mission;
import com.highbrowape.demo.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ConversationRepository extends JpaRepository<Conversation,Long> {

    List<Conversation> findByMission(Mission mission);


    List<Conversation> findByMissionOrderByStartedOnDesc(Mission mission);


    @Query("SELECT " +
            "    new com.highbrowape.demo.dto.output.UserCommentChartData(count(c),c.month,c.date) " +
            "FROM " +
            "    Conversation c group by c.date")
    List<UserCommentChartData> countOrderByDate(String email);

    @Query("SELECT " +
            "    new com.highbrowape.demo.dto.output.UserCommentChartData(count(c),c.month,c.date) " +
            "FROM " +
            "    Conversation c group by c.month")
    List<UserCommentChartData> countOrderByMonth(String email);

}
