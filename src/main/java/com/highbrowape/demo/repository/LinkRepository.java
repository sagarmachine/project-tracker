package com.highbrowape.demo.repository;

import com.highbrowape.demo.entity.Link;
import com.highbrowape.demo.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LinkRepository extends JpaRepository<Link,Long> {

}
