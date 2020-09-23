package com.highbrowape.demo.repository;

import com.highbrowape.demo.entity.Objective;
import com.highbrowape.demo.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ObjectiveRepository extends JpaRepository<Objective,Long> {

}
