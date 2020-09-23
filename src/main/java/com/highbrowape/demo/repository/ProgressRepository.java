package com.highbrowape.demo.repository;

import com.highbrowape.demo.entity.Progress;
import com.highbrowape.demo.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProgressRepository extends JpaRepository<Progress,Long> {

}
