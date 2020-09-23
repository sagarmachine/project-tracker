package com.highbrowape.demo.repository;

import com.highbrowape.demo.entity.Comment;
import com.highbrowape.demo.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment,Long> {

}
