package com.highbrowape.demo.repository;

import com.highbrowape.demo.entity.Conversation;
import com.highbrowape.demo.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConversationRepository extends JpaRepository<Conversation,Long> {

}
