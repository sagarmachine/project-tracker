package com.highbrowape.demo.repository;

import com.highbrowape.demo.dto.output.UserCommentChartData;
import com.highbrowape.demo.entity.Comment;
import com.highbrowape.demo.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment,Long> {


    @Query("SELECT " +
            "    new com.highbrowape.demo.dto.output.UserCommentChartData(count(c),c.month,c.date) " +
            "FROM " +
            "  Comment c group by c.date")
    List<UserCommentChartData> countOrderByDate(String email);



    @Query("SELECT " +
            "    new com.highbrowape.demo.dto.output.UserCommentChartData(count(c),c.month,c.date) " +
            "FROM " +
            "    Comment c group by c.month")
    List<UserCommentChartData> countOrderByMonth(String email);

}
