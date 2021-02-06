package com.highbrowape.demo.repository;

import com.highbrowape.demo.entity.User;
import com.highbrowape.demo.entity.UserInteraction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserInteractionRepository extends JpaRepository<UserInteraction,Long> {

    boolean existsByDateAndMonthAndYearAndUserEmail(int date, int month, int year, String user);
Optional<UserInteraction> findByDateAndMonthAndYearAndUser(int date, int month, int year, User user);

List<UserInteraction> findByUserEmail(String email);
}
